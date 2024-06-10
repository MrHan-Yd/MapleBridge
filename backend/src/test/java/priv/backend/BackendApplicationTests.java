//package priv.backend;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.mongodb.client.MongoClient;
//import jakarta.annotation.Resource;
//import lombok.Data;
//import org.apache.kafka.clients.admin.AdminClient;
//import org.apache.kafka.clients.admin.AdminClientConfig;
//import org.apache.kafka.clients.admin.DescribeClusterResult;
//import org.apache.kafka.clients.admin.KafkaAdminClient;
//import org.apache.kafka.common.KafkaFuture;
//import org.apache.kafka.common.Node;
//import org.apache.kafka.common.utils.AppInfoParser;
//import org.bson.Document;
//import org.elasticsearch.client.Request;
//import org.elasticsearch.client.Response;
//import org.elasticsearch.client.RestClient;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.connection.RedisConnection;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.jdbc.core.JdbcTemplate;
//import priv.backend.config.task.UserPreferenceCollectionTask;
//import priv.backend.config.task.TaskExecutionPlan;
//import priv.backend.domain.PageBean;
//import priv.backend.domain.mongo.dto.MongoType;
//import priv.backend.domain.mongo.dto.MongoUserPreferences;
//import priv.backend.enumeration.StatusEnum;
//import priv.backend.util.CurrentUtils;
//import oshi.SystemInfo;
//import oshi.hardware.CentralProcessor;
//import oshi.hardware.GlobalMemory;
//import oshi.software.os.OSFileStore;
//import oshi.software.os.OperatingSystem;
//import priv.backend.util.SystemUtils;
//import priv.backend.util.TimeUtils;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//
//
//
//import java.io.File;
//import java.io.IOException;
//import java.security.SecureRandom;
//import java.sql.SQLException;
//import java.time.LocalDateTime;
//import java.util.*;
//import java.util.jar.Attributes;
//
//@SpringBootTest
//class BackendApplicationTests {
//
//    @Test
//    void contextLoads() {
//        System.out.println(StatusEnum.NORMAL.STATE);
//    }
//
//    /* TODO: Written by - Han Yongding 2023/09/06 生成一个Security安全key */
//    @Test
//    void KeyGenerator() {
//        // 示例，生成一个长度为32的安全密钥
//        String key = generateSecurityKey(32);
//        System.out.println(key);
//    }
//
//    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//
//    public static String generateSecurityKey(int length) {
//        SecureRandom random = new SecureRandom();
//        StringBuilder sb = new StringBuilder(length);
//
//        for (int i = 0; i < length; i++) {
//            int randomIndex = random.nextInt(CHARACTERS.length());
//            char randomChar = CHARACTERS.charAt(randomIndex);
//            sb.append(randomChar);
//        }
//
//        return sb.toString();
//    }
//
//    /* TODO: Written by - Han Yongding 2024/03/09 分页实体测试 */
//    @Test
//    void PageTest() {
//        PageBean pageBean = new PageBean();
//        System.out.println(pageBean);
//    }
//
//    /* TODO: Written by - Han Yongding 2024/04/15 版本更新 */
//    @Test
//    void selfIncreasing() {
//        Double a = 0.9;
//        System.out.println(CurrentUtils.versionUpdate(a));
//    }
//
//    /* TODO: Written by - Han Yongding 2024/04/15 点赞更新 */
//    @Test
//    void updateLikes() {
//        Integer a = 1;
//        System.out.println(CurrentUtils.countUpdate(a));
//    }
//
//    @Resource
//    private UserPreferenceCollectionTask userPreferenceTasks;
//
//    /* TODO: Written by - Han Yongding 2024/05/10 调用测试Task注解 */
////    @Test
////    void callTaskTest() {
////        userPreferenceTasks.executeTask() ;
////    }
//
//    /* TODO: Written by - Han Yongding 2024/05/13 测试动态代理任务执行计划 */
////    @Test
////    void test() {
////        userPreferenceTasks.scheduleTask();
////    }
//
//    /**
//     * 使用基于内容的推荐算法推荐用户可能喜欢的帖子类型。
//     *
//     * @param userPreferencesList 包含用户点击信息的列表
//     * @return 推荐结果，每个元素是一个 Recommendation 对象，包含用户ID和推荐的帖子类型列表
//     */
//    public List<Recommendation> recommend(List<MongoUserPreferences> userPreferencesList) {
//        List<Recommendation> recommendations = new ArrayList<>();
//
//        // 遍历用户点击信息列表，为每个用户推荐帖子类型
//        for (MongoUserPreferences userPreferences : userPreferencesList) {
//            String userId = userPreferences.getUserId();
//            List<MongoType> typeList = userPreferences.getTypeList();
//
//            // 创建最大堆来存储每个用户的点击类型，并按点击次数降序排列
//            PriorityQueue<MongoType> maxHeap = new PriorityQueue<>(Comparator.comparingInt(MongoType::getCount).reversed());
//            maxHeap.addAll(typeList);
//
//            // 获取用户点击次数最多的两个帖子类型作为推荐结果
//            List<String> recommendedTypes = new ArrayList<>();
//            int count = 0;
//            while (!maxHeap.isEmpty() && count < 2) {
//                recommendedTypes.add(maxHeap.poll().getTypeId());
//                count++;
//            }
//
//            // 将用户ID和推荐的帖子类型列表添加到推荐结果列表中
//            recommendations.add(new Recommendation(userId, recommendedTypes));
//        }
//
//        return recommendations;
//    }
//
//    @Test
//    void test2() {
//        List<MongoUserPreferences> list = new ArrayList<>();
//
//        // 添加第一个用户的点击信息
//        MongoUserPreferences userPreferences1 = new MongoUserPreferences();
//        userPreferences1.setUserId("12123");
//        List<MongoType> typeList1 = new ArrayList<>();
//
//        MongoType mongoType1 = new MongoType();
//        mongoType1.setTypeId("1765981996910108674");
//        mongoType1.setCount(15);
//        typeList1.add(mongoType1);
//
//        MongoType mongoType2 = new MongoType();
//        mongoType2.setTypeId("1765982034910502913");
//        mongoType2.setCount(6);
//        typeList1.add(mongoType2);
//
//        MongoType mongoType3 = new MongoType();
//        mongoType3.setTypeId("1765981952316268545");
//        mongoType3.setCount(10);
//        typeList1.add(mongoType3);
//
//        userPreferences1.setTypeList(typeList1);
//        list.add(userPreferences1);
//
//        // 添加第二个用户的点击信息
//        MongoUserPreferences userPreferences2 = new MongoUserPreferences();
//        userPreferences2.setUserId("23456");
//        List<MongoType> typeList2 = new ArrayList<>();
//
//        MongoType mongoType4 = new MongoType();
//        mongoType4.setTypeId("1765981996910108674");
//        mongoType4.setCount(10);
//        typeList2.add(mongoType4);
//
//        MongoType mongoType5 = new MongoType();
//        mongoType5.setTypeId("1765982034910502913");
//        mongoType5.setCount(16);
//        typeList2.add(mongoType5);
//
//        MongoType mongoType6 = new MongoType();
//        mongoType6.setTypeId("1765981952316268545");
//        mongoType6.setCount(30);
//        typeList2.add(mongoType6);
//
//        userPreferences2.setTypeList(typeList2);
//        list.add(userPreferences2);
//
//        List<Recommendation> recommendations = recommend(list);
//
//        // 输出推荐结果
//        for (Recommendation recommendation : recommendations) {
//            System.out.println("User ID: " + recommendation.getUserId() + ", Recommended Types: " + recommendation.getRecommendedTypes());
//        }
//    }
//
//    @Data
//    class Recommendation {
//        private String userId;
//        private List<String> recommendedTypes;
//
//        public Recommendation(String userId, List<String> recommendedTypes) {
//            this.userId = userId;
//            this.recommendedTypes = recommendedTypes;
//        }
//
//        // Getters
//    }
//
//    @Resource
//    TaskExecutionPlan taskTaskManagement ;
//    @Test
//    void test3() {
//        taskTaskManagement.scheduleTask() ;
//    }
//
//    /* TODO: Written by - Han Yongding 2024/05/14 测试从MongoDB中获取数据并处理 */
//
//
//    @Test
//    void test4() {
//        System.out.println(check("abcba"));
//    }
//
//    private boolean check(String str) {
//        int l = 0;
//        int n = str.length() - 1;
//        while (l < n) {
//            if (str.charAt(l) == str.charAt(n)) {
//                l++;
//                n--;
//            } else {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    @Test
//    void getSystemProperties() {
//        Properties props = System.getProperties();
//
//        props.forEach((key, value) -> {
//            System.out.println(key + ": " + value);
//        });
//    }
//
//    @Test
//    void test5() throws InterruptedException {
//        SystemInfo systemInfo = new SystemInfo();
//        CentralProcessor processor = systemInfo.getHardware().getProcessor();
//        GlobalMemory memory = systemInfo.getHardware().getMemory();
//        OperatingSystem os = systemInfo.getOperatingSystem();
//        System.out.println(os.getFamily());
//        System.out.println(os.getManufacturer());
//        System.out.println(os.getVersionInfo());
//        OSFileStore fileStore = os.getFileSystem().getFileStores().get(0);
//
//        // 获取内存使用情况
//        long totalMemory = memory.getTotal() / (1024 * 1024 * 1024); // 转换为 GB
//        long usedMemory = (memory.getTotal() - memory.getAvailable()) / (1024 * 1024 * 1024); // 转换为 GB
//
//        System.out.println("已使用内存: " + usedMemory + " GB");
//        System.out.println("总内存大小: " + totalMemory + " GB");
//
//        // 获取 CPU 型号
//        String cpuModel = processor.getProcessorIdentifier().getName();
//        System.out.println("CPU 型号: " + cpuModel);
//
//        System.out.println(processor.getPhysicalProcessorCount());
//        // 获取 CPU 核心数
//        int availableProcessors = processor.getLogicalProcessorCount();
//        System.out.println("CPU 核心数: " + availableProcessors);
//
//        // 获取 CPU 使用情况
//        long[] prevTicks = processor.getSystemCpuLoadTicks();
//        // 等待一秒钟以获取CPU使用率
//        Thread.sleep(1000);
//        long[] ticks = processor.getSystemCpuLoadTicks();
//        double cpuLoad = processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100; // 获取CPU使用率并转换为百分比
//        System.out.println("CPU 使用情况: " + String.format("%.2f", cpuLoad) + "%");
//        // 持续监控 CPU 使用情况
////        for (int i = 0; i < 10; i++) { // 监控10次，每次间隔1秒
////            long[] prevTicks = processor.getSystemCpuLoadTicks();
////            Thread.sleep(1000); // 等待1秒钟
////            long[] ticks = processor.getSystemCpuLoadTicks();
////            double cpuLoad = processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100; // 获取CPU使用率并转换为百分比
////            System.out.println("CPU 使用情况: " + String.format("%.2f", cpuLoad) + "%");
////        }
//
//        // 获取硬盘空间信息
//        long totalDiskSpace = fileStore.getTotalSpace() / (1024 * 1024 * 1024); // 转换为 GB
//        long freeDiskSpace = fileStore.getUsableSpace() / (1024 * 1024 * 1024); // 转换为 GB
//
//        System.out.println("总硬盘空间: " + totalDiskSpace + " GB");
//        System.out.println("可用硬盘空间: " + freeDiskSpace + " GB");
//
//        // 获取项目所在硬盘的使用情况
//        File projectDir = new File(System.getProperty("user.dir"));
//        long projectTotalSpace = projectDir.getTotalSpace() / (1024 * 1024 * 1024); // 转换为 GB
//        long projectFreeSpace = projectDir.getUsableSpace() / (1024 * 1024 * 1024); // 转换为 GB
//
//        System.out.println("项目所在硬盘总空间: " + projectTotalSpace + " GB");
//        System.out.println("项目所在硬盘可用空间: " + projectFreeSpace + " GB");
//    }
//
//    @Test
//    void test6() throws InterruptedException {
//        System.out.println(SystemUtils.getServerParameters("1"));
//    }
//
//    @Test
//    void test7() {
//        System.out.println(TimeUtils.isExpired(LocalDateTime.parse("2024-05-29T00:01:54.019483100")));
//    }
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    private RestClient restClient;
//
//    @Autowired
//    private RedisConnectionFactory redisConnectionFactory;
//
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//
//
//
//    @Test
//    void test8() throws Exception {
//        String javaVersion = System.getProperty("java.version");
//        System.out.println(javaVersion);
//        String mariadbVersion = jdbcTemplate.queryForObject("SELECT VERSION()", String.class);
//        System.out.println(mariadbVersion);
//        try {
//            Request request = new Request("GET", "/");
//            Response response = restClient.performRequest(request);
//            ObjectMapper mapper = new ObjectMapper();
//            Map<String, Object> responseMap = mapper.readValue(response.getEntity().getContent(), Map.class);
//            Map<String, Object> versionMap = (Map<String, Object>) responseMap.get("version");
//            System.out.println(versionMap.get("number").toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            Request request = new Request("GET", "/_nodes/plugins");
//            Response response = restClient.performRequest(request);
//            ObjectMapper mapper = new ObjectMapper();
//            Map<String, Object> responseMap = mapper.readValue(response.getEntity().getContent(), Map.class);
//            Map<String, Object> nodes = (Map<String, Object>) responseMap.get("nodes");
//
//            for (Map.Entry<String, Object> node : nodes.entrySet()) {
//                Map<String, Object> nodeInfo = (Map<String, Object>) node.getValue();
//                List<Map<String, Object>> plugins = (List<Map<String, Object>>) nodeInfo.get("plugins");
//
//                for (Map<String, Object> plugin : plugins) {
//                    System.out.println(plugin.get("name") + ":" +plugin.get("version").toString());
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        RedisConnection connection = redisConnectionFactory.getConnection();
//        Properties info = connection.info();
//        System.out.println(info.getProperty("redis_version"));
//
//        Map<String, Object> rabbitMQInformation = getRabbitMqVersion();
//        System.out.println(rabbitMQInformation.get("platform"));
//        System.out.println(rabbitMQInformation.get("version"));
//
//        System.out.println("Kafka-" + AppInfoParser.getVersion());
//
//
//        getMongoDBInfo();
//    }
//
//    public Map<String, Object> getRabbitMqVersion() {
//        return rabbitTemplate.execute(channel -> channel.getConnection().getServerProperties());
//    }
//
//    @Autowired
//    private KafkaProperties kafkaProperties;
//
//    public String getKafkaVersion() throws Exception {
//        Properties properties = new Properties();
//        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
//        try (AdminClient adminClient = KafkaAdminClient.create(properties)) {
//            DescribeClusterResult describeClusterResult = adminClient.describeCluster();
//            KafkaFuture<Node> kafkaVersionFuture = describeClusterResult.controller();
//            System.out.println(kafkaVersionFuture.get());
//            KafkaFuture<Node> stringFuture = kafkaVersionFuture.thenApply(node -> {
//                // Convert Node to String
//                return node;
//            });
////            System.out.println(stringFuture.);
//            return null;
//        }
//    }
//
//    @Autowired
//    private MongoClient mongoClient;
//
//    public void getMongoDBInfo() {
//        String serverVersion = mongoClient.getDatabase("admin")
//                .runCommand(new Document("buildInfo", 1))
//                .getString("version");
//        System.out.println("MongoDB server version: " + serverVersion);
//        System.out.println("MongoDB server address: " + mongoClient.getClusterDescription().getClusterSettings().getHosts());
//    }
//
//}
