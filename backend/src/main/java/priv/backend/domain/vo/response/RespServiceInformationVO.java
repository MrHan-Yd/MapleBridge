package priv.backend.domain.vo.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 响应前端服务器信息
 * @CreateDate :  2024-06-02 16:47
 */
@Data
public class RespServiceInformationVO {
    /* TODO: Written by - Han Yongding 2024/06/02 Java版本 */
    private String javaVersion ;
    /* TODO: Written by - Han Yongding 2024/06/02 MariaDB版本 */
    private String mariadbVersion ;
    /* TODO: Written by - Han Yongding 2024/06/02 ElasticSearch版本 */
    private String elasticsearchVersion ;
    /* TODO: Written by - Han Yongding 2024/06/02 Elasticsearch分词器版本  */
    private String elasticsearchAnalyzer ;
    /* TODO: Written by - Han Yongding 2024/06/02 Redis版本  */
    private String redisVersion ;
    /* TODO: Written by - Han Yongding 2024/06/02 RabbitMQ Erlang/OTP版本  */
    private String rabbitmqErlangOTPVersion ;
    /* TODO: Written by - Han Yongding 2024/06/02 RabbitMQ版本  */
    private String rabbitmqVersion ;
    /* TODO: Written by - Han Yongding 2024/06/02 Kafka版本  */
    private String kafkaVersion ;
    /* TODO: Written by - Han Yongding 2024/06/02 MongoDB版本  */
    private String mongodbVersion ;
}
