package priv.backend.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import priv.backend.domain.mongo.dto.MongoUserPreferences;
import priv.backend.domain.mongo.vo.RestMongoPostTypeVO;

import java.util.function.Supplier;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : Kafka生产者工具类
 * @CreateDate :  2024-05-07 21:17
 */
@Component
public class KafkaProducerUtils {

    /* TODO: Written by - Han Yongding 2024/05/07 注入kafka模板 */
    private final KafkaTemplate<String, Object> template;

    @Autowired
    public KafkaProducerUtils(KafkaTemplate<String, Object> kafkaTemplate) {
        this.template = kafkaTemplate;
    }

    /* TODO: Written by - Han Yongding 2024/05/07 发送消息 */
    public <T> void sendMessage(String topic, T message) {
        template.send(topic, message);
    }

}
