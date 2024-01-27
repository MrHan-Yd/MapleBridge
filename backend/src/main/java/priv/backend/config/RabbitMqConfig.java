package priv.backend.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 配置消息队列
 * @CreateDate :  2024-01-09 22:11
 */

@Configuration
public class RabbitMqConfig {
    @Bean("emailQueue")
    public Queue eamilQueue() {
        return QueueBuilder
                .durable("mail")
                .build() ;
    }
}
