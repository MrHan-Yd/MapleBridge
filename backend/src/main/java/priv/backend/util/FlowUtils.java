package priv.backend.util;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 限流工具类
 * @CreateDate :  2024-01-09 22:17
 */
@Component
public class FlowUtils {
    /** TODO: Written by - Han Yongding 2023/09/18 注入字符串redis */
    @Resource
    private StringRedisTemplate template ;

    /** TODO: Written by - Han Yongding 2023/09/18 针对于单次的频率限制 */
    public boolean limitOnceCheck(String key, int codeTime) {
        /* TODO: Written by - Han Yongding 2023/09/18 如果正在冷却状态 */
        if (Boolean.TRUE.equals(template.hasKey(key))) {
            return false ;
        } else {
            /* TODO: Written by - Han Yongding 2023/09/18 如果不在冷却状态(没有被封禁、限制) */
            template.opsForValue()
                    .set(key, "", codeTime, TimeUnit.SECONDS) ;
            return true ;
        }
    }
}
