package priv.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 服务信息
 * @CreateDate :  2024-06-02 16:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("serviceInformation")
public class ServiceInformation implements BaseData {
    @Id
    private String id ;
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
    /* TODO: Written by - Han Yongding 2024/06/02 逻辑过期时间 */
    private LocalDateTime expirationTime ;
}
