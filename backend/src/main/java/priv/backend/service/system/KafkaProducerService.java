package priv.backend.service.system;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : Kafka生产者
 * @CreateDate :  2024-01-09 21:51
 */
//@Slf4j
//@Service
//public class KafkaProducerService {
//    private final KafkaTemplate<String, String> kafkaTemplate;
//
//    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//    public void sendMessage(String topic, String message) {
//        log.info("Sending message to topic: {}, message: {}", topic, message);
//        kafkaTemplate.send(topic, message);
//        log.info("Message sent successfully");
//    }
//}
