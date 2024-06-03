package priv.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
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

    /**
     * 配置用于处理 ServerParameters 对象的 RedisTemplate
     *
     * @param redisConnectionFactory Redis 连接工厂
     * @return 配置好的 RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        /* TODO: Written by - Han Yongding 2024/05/29 配置 Jackson 序列化器，支持 Java 8 日期和时间 API */
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        /* 使用 GenericJackson2JsonRedisSerializer 进行序列化和反序列化 */
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        /* 设置键的序列化器为 StringRedisSerializer */
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        /* 设置值和哈希值的序列化器为 GenericJackson2JsonRedisSerializer */
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);


        template.afterPropertiesSet();
        return template;
    }
}
