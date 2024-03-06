package priv.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : Redis配置
 * @CreateDate :  2024-02-17 17:28
 */
@Configuration
public class RedisConfig {

    /** TODO: Written by - Han Yongding 2024/02/17 Redis序列化 */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory,
                                                       ObjectMapper objectMapper) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // 设置键的序列化器
        template.setKeySerializer(new StringRedisSerializer());

        // 设置值的序列化器，使用Jackson2JsonRedisSerializer的构造函数
        Jackson2JsonRedisSerializer<Object> jacksonSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

        template.setValueSerializer(jacksonSerializer);

        // 设置Hash键的序列化器
        template.setHashKeySerializer(new StringRedisSerializer());

        // 设置Hash值的序列化器，同样使用Jackson2JsonRedisSerializer
        template.setHashValueSerializer(jacksonSerializer);

        template.afterPropertiesSet();
        return template;
    }
}
