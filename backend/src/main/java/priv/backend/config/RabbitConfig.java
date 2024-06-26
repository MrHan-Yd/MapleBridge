package priv.backend.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 配置消息队列
 * @CreateDate :  2023-08-13 20:04
 */
@Configuration
public class RabbitConfig {
    /** TODO: Written by - Han Yongding 2024/02/28 配置RabbitMq队列 */
    @Bean("emailQueue")
    public Queue emailQueue() {
        return QueueBuilder
                .durable("mail")
                .build() ;
    }

    /* TODO: Written by - Han Yongding 2024/03/28 另一个发送通知邮件MQ */
    @Bean("emailNoticeQueue")
    public Queue emailNoticeQueue() {
        return QueueBuilder
                .durable("notice")
                .build() ;
    }

    /* TODO: Written by - Han Yongding 2024/04/03 Post同步ES使用 */
    @Bean("postSyncESQueue")
    public Queue postSyncESQueue() {
        return QueueBuilder
                .durable("postSyncES")
                .build();
    }

    /* TODO: Written by - Han Yongding 2024/04/11 客户端User同步ES使用 */
    @Bean("clientUserSyncQueue")
    public Queue clientUserSyncQueue() {
        return QueueBuilder
                .durable("clientUserSyncES")
                .build() ;
    }

    /* TODO: Written by - Han Yongding 2024/04/15 post Like 重试 */
    @Bean("postLikeRetryQueue")
    public Queue postLikeRetryQueue() {
        return QueueBuilder
               .durable("postLikeRetry")
               .build() ;
    }

    /* TODO: Written by - Han Yongding 2024/04/15 post Like 重试 */
    @Bean("postUnLikeRetryQueue")
    public Queue postUnLikeRetryQueue() {
        return QueueBuilder
                .durable("postUnLikeRetry")
                .build() ;
    }

    /* TODO: Written by - Han Yongding 2024/05/05 评论表 点赞重试 */

    @Bean("commentLikeRetryQueue")
    public Queue commentLikeRetryQueue() {
        return QueueBuilder
               .durable("commentLikeRetry")
               .build() ;
    }

    /* TODO: Written by - Han Yongding 2024/05/05 评论表 取消点赞重试 */

    @Bean("commentUnLikeRetryQueue")
    public Queue commentUnLikeRetryQueue() {
        return QueueBuilder
               .durable("commentUnLikeRetry")
               .build() ;
    }

    /** TODO: Written by - Han Yongding 2024/02/28 消息转换器 */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
