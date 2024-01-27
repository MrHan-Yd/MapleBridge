package priv.backend.listener;

import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : Kafka消费者
 * @CreateDate :  2024-01-09 21:47
 */
@Service
public class KafkaListener {
    @org.springframework.kafka.annotation.KafkaListener(topics = "my-topic", groupId = "my-group")
    public void listen(String message) {
        System.out.println("Received message: " + message);
        // Process the received message as needed
    }
}
