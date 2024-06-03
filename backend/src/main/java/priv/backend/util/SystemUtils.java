package priv.backend.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import org.apache.kafka.common.utils.AppInfoParser;
import org.bson.Document;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import priv.backend.domain.ServerParameters;
import priv.backend.domain.ServiceInformation;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 系统常用工具类
 * @CreateDate :  2024-05-26 0:45
 */
@Component
public class SystemUtils {


    /* TODO: Written by - Han Yongding 2024/06/02 mongodb客户端 */
    private final MongoClient mongoClient;

    /* TODO: Written by - Han Yongding 2024/06/02 Kafka属性 */
    private final KafkaProperties kafkaProperties ;

    /* TODO: Written by - Han Yongding 2024/06/02 RabbitMQ模板 */
    private final RabbitTemplate rabbitTemplate;

    /* TODO: Written by - Han Yongding 2024/06/02 Redis连接工厂 */
    private final RedisConnectionFactory redisConnectionFactory;

    /* TODO: Written by - Han Yongding 2024/06/02 Elasticsearch客户端 */
    private final RestClient restClient;

    /* TODO: Written by - Han Yongding 2024/06/02 JDBC模板 */
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SystemUtils(MongoClient mongoClient,
                       KafkaProperties kafkaProperties,
                       RabbitTemplate rabbitTemplate,
                       RedisConnectionFactory redisConnectionFactory,
                       RestClient restClient,
                       JdbcTemplate jdbcTemplate) {
        this.mongoClient = mongoClient;
        this.kafkaProperties = kafkaProperties;
        this.rabbitTemplate = rabbitTemplate;
        this.redisConnectionFactory = redisConnectionFactory;
        this.restClient = restClient;
        this.jdbcTemplate = jdbcTemplate;
    }

    /* TODO: Written by - Han Yongding 2024/05/26 获取服务器参数 */
    public static ServerParameters getServerParameters(String key) throws InterruptedException {
        /* TODO: Written by - Han Yongding 2024/05/26 返回对象 */
        ServerParameters serverParameters = new ServerParameters();
        serverParameters.setId(key);
        SystemInfo systemInfo = new SystemInfo();
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        OperatingSystem os = systemInfo.getOperatingSystem();
        /* TODO: Written by - Han Yongding 2024/05/26 获取系统名称 */
        serverParameters.setSystemName(os.getFamily());
        /* TODO: Written by - Han Yongding 2024/05/26 获取系统制造商 */
        serverParameters.setSystemManufacturer(os.getManufacturer());
        /* TODO: Written by - Han Yongding 2024/05/26 获取系统版本 */
        serverParameters.setSystemVersion(os.getVersionInfo().getVersion());
        /* TODO: Written by - Han Yongding 2024/05/26 获取系统类型 */
        serverParameters.setSystemType(os.getVersionInfo().getCodeName());
        /* TODO: Written by - Han Yongding 2024/05/26 获取系统内部版本号 */
        serverParameters.setSystemInternalVersionNumber(os.getVersionInfo().getBuildNumber());

        /* TODO: Written by - Han Yongding 2024/05/26 获取内存使用情况 */
        Double totalMemory = memory.getTotal() / (1024.0 * 1024.0 * 1024.0); // 转换为 GB
        Double usedMemory = (memory.getTotal() - memory.getAvailable()) / (1024.0 * 1024.0 * 1024.0); // 转换为 GB

        // 格式化输出
        DecimalFormat df = new DecimalFormat("#.#");
        String formattedTotalMemory = df.format(totalMemory);
        String formattedUsedMemory = df.format(usedMemory);

        /* TODO: Written by - Han Yongding 2024/05/26 已用内存 */
        serverParameters.setUsedMemory(Double.valueOf(formattedUsedMemory)) ;
        /* TODO: Written by - Han Yongding 2024/05/26 总内存大小 */
        serverParameters.setTotalMemory(Double.valueOf(formattedTotalMemory)) ;

        /* TODO: Written by - Han Yongding 2024/05/26 获取 CPU 型号 */
        serverParameters.setCpuModel(processor.getProcessorIdentifier().getName().trim());

        /* TODO: Written by - Han Yongding 2024/05/26 获取 CPU 核心数  */
        serverParameters.setCpuPhysicalProcessorNums(processor.getPhysicalProcessorCount());

        /* TODO: Written by - Han Yongding 2024/05/26 获取 CPU 线程数 */

        serverParameters.setCpuLogicProcessorNums(processor.getLogicalProcessorCount());


        // 获取 CPU 使用情况
//        long[] prevTicks = processor.getSystemCpuLoadTicks();

        // 等待一秒钟以获取CPU使用率
//        Thread.sleep(500);
//        double cpuLoad = processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100; // 获取CPU使用率并转换为百分比
        double cpuLoad = Arrays.stream(processor.getProcessorCpuLoad(500)).average().getAsDouble() * 100D ;
        cpuLoad = cpuLoad == 0.0 ? 0.1 : cpuLoad;// 防止出现CPU使用率为0的情况
        /* TODO: Written by - Han Yongding 2024/05/26 获取 CPU 使用情况 */
        // 持续监控 CPU 使用情况
//        double cpuRs = 0 ;
//        for (int i = 0; i < 10; i++) { // 监控10次，每次间隔1秒
//            long[] prevTicks = processor.getSystemCpuLoadTicks();
//            processor.getcpu
//
//            cpuRs += processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100; // 获取CPU使用率并转换为百分比
//        }

        serverParameters.setCpuLoad(Double.valueOf(String.format("%.2f", cpuLoad)));

        OSFileStore fileStore = os.getFileSystem().getFileStores().get(0);
        /* TODO: Written by - Han Yongding 2024/05/26 获取系统盘空间信息 */
        long totalDiskSpace = fileStore.getTotalSpace() / (1024 * 1024 * 1024); // 转换为 GB
        long freeDiskSpace = fileStore.getUsableSpace() / (1024 * 1024 * 1024); // 转换为 GB

        /* TODO: Written by - Han Yongding 2024/05/26 获取系统盘总大小 */
        serverParameters.setSystemTotalDiskSpace((double) totalDiskSpace);

        /* TODO: Written by - Han Yongding 2024/05/26 获取系统盘可用大小 */
        serverParameters.setSystemAvailableDiskSpace((double) freeDiskSpace);

        /* TODO: Written by - Han Yongding 2024/05/26 获取项目盘信息 */
        File projectDir = new File(System.getProperty("user.dir"));
        long projectTotalSpace = projectDir.getTotalSpace() / (1024 * 1024 * 1024); // 转换为 GB
        long projectFreeSpace = projectDir.getUsableSpace() / (1024 * 1024 * 1024); // 转换为 GB

        /* TODO: Written by - Han Yongding 2024/05/26 获取项目盘总大小 */
        serverParameters.setProjectTotalDiskSpace((double) projectTotalSpace);
        /* TODO: Written by - Han Yongding 2024/05/26 获取项目盘可用大小 */
        serverParameters.setProjectAvailableDiskSpace((double) projectFreeSpace);

        /* TODO: Written by - Han Yongding 2024/05/28 逻辑过期时间 */
        serverParameters.setExpirationTime(TimeUtils.addFiveMinutes(LocalDateTime.now()));

        return serverParameters;
    }


    /* TODO: Written by - Han Yongding 2024/06/02 获取服务信息 */
    public ServiceInformation getServiceInformation(String key) {
        ServiceInformation serviceInformation = new ServiceInformation();
        serviceInformation.setId(key);
        /* TODO: Written by - Han Yongding 2024/06/02 JDK版本号 */
        serviceInformation.setJavaVersion(getJavaVersion());
        /* TODO: Written by - Han Yongding 2024/06/02 MariaDB数据库服务版本号 */
        String[] mariaDBVersion = getMariaDBVersion().split("-");
        serviceInformation.setMariadbVersion(mariaDBVersion[1] + ":" + mariaDBVersion[0]);
        /* TODO: Written by - Han Yongding 2024/06/02 Elasticsearch版本号 */
        serviceInformation.setElasticsearchVersion(getElasticSearchVersion());
        /* TODO: Written by - Han Yongding 2024/06/02 Elasticsearch分词器版本号 */
        serviceInformation.setElasticsearchAnalyzer(getElasticSearchAnalyzerVersion());
        /* TODO: Written by - Han Yongding 2024/06/02 Redis版本号 */
        serviceInformation.setRedisVersion(getRedisVersion());
        /* TODO: Written by - Han Yongding 2024/06/02 RabbitMQ服务属性 */
        Map<String, Object> rabbitMQVersion = getRabbitMQVersion();
        /* TODO: Written by - Han Yongding 2024/06/02 RabbitMQ版本号 */
        serviceInformation.setRabbitmqVersion(rabbitMQVersion.get("version").toString());
        /* TODO: Written by - Han Yongding 2024/06/02 RabbitMQ Erlang/OTP版本号 */
        serviceInformation.setRabbitmqErlangOTPVersion(rabbitMQVersion.get("platform").toString());
        /* TODO: Written by - Han Yongding 2024/06/02 Kafka版本 */
        serviceInformation.setKafkaVersion(AppInfoParser.getVersion());
        /* TODO: Written by - Han Yongding 2024/06/02 MongoDB版本号 */
        serviceInformation.setMongodbVersion(getMongoDBVersion());
        /* TODO: Written by - Han Yongding 2024/06/02 逻辑过期时间 */
        serviceInformation.setExpirationTime(TimeUtils.addThirtyMinutes(LocalDateTime.now()));
        return serviceInformation;
    }

    /* TODO: Written by - Han Yongding 2024/06/02 获取JDK版本号 */
    private String getJavaVersion() {
        return System.getProperty("java.version");
    }

    /* TODO: Written by - Han Yongding 2024/06/02 获取MariaDB数据库服务版本号 */
    private String getMariaDBVersion() {
        return jdbcTemplate.queryForObject("SELECT VERSION()", String.class) ;
    }

    /* TODO: Written by - Han Yongding 2024/06/02 获取Elasticsearch版本号 */
    private String getElasticSearchVersion() {
        try {
            Request request = new Request("GET", "/");
            Response response = restClient.performRequest(request);
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> responseMap = mapper.readValue(response.getEntity().getContent(), Map.class);
            Map<String, Object> versionMap = (Map<String, Object>) responseMap.get("version");
            return versionMap.get("number").toString();
        } catch (IOException e) {
            LogUtils.error(this.getClass(), "获取Elasticsearch版本号失败:" + e);
        }
        /* TODO: Written by - Han Yongding 2024/06/02 没有找到Elasticsearch版本号 */
        return null ;
    }

    /* TODO: Written by - Han Yongding 2024/06/02 获取Elasticsearch分词器版本号 */
    private String getElasticSearchAnalyzerVersion() {
        try {
            Request request = new Request("GET", "/_nodes/plugins");
            Response response = restClient.performRequest(request);
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> responseMap = mapper.readValue(response.getEntity().getContent(), Map.class);
            Map<String, Object> nodes = (Map<String, Object>) responseMap.get("nodes");

            for (Map.Entry<String, Object> node : nodes.entrySet()) {
                Map<String, Object> nodeInfo = (Map<String, Object>) node.getValue();
                List<Map<String, Object>> plugins = (List<Map<String, Object>>) nodeInfo.get("plugins");

                for (Map<String, Object> plugin : plugins) {
                    return plugin.get("name") + ":" + plugin.get("version").toString() ;
                }
            }
        } catch (IOException e) {
            LogUtils.error(this.getClass(), "获取Elasticsearch分词器版本号失败:" + e);
        }
        /* TODO: Written by - Han Yongding 2024/06/02 没有找到Elasticsearch分词器版本号 */
        return null ;
    }

    /* TODO: Written by - Han Yongding 2024/06/02 获取Redis版本号 */
    private String getRedisVersion() {
        RedisConnection connection = redisConnectionFactory.getConnection();
        Properties info = connection.info();
        return info.getProperty("redis_version");
    }

    /* TODO: Written by - Han Yongding 2024/06/02 获取RabbitMQ版本号 */
    private Map<String, Object> getRabbitMQVersion() {
        return rabbitTemplate.execute(channel -> channel.getConnection().getServerProperties());
    }

    /* TODO: Written by - Han Yongding 2024/06/02 获取MongoDB版本号 */
     private String getMongoDBVersion() {
        return  mongoClient.getDatabase("admin")
                .runCommand(new Document("buildInfo", 1))
                .getString("version");
    }
}
