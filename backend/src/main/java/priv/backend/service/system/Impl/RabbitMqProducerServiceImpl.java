package priv.backend.service.system.Impl;

import jakarta.annotation.Resource;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import priv.backend.service.system.RabbitMqProducerService;
import priv.backend.util.Const;
import priv.backend.util.FlowUtils;
import priv.backend.util.RandomStringUtil;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : RabbitMQ生产者实现类
 * @CreateDate :  2024-01-10 13:02
 */
@Service
public class RabbitMqProducerServiceImpl implements RabbitMqProducerService {

    /** TODO: Written by - Han Yongding 2023/09/18 注入消息队列 */
    @Resource
    private AmqpTemplate amqpTemplate ;

    /** TODO: Written by - Han Yongding 2023/09/18 注入redis */
    @Resource
    private StringRedisTemplate stringRedisTemplate ;

    /** TODO: Written by - Han Yongding 2023/09/18 注入限流工具类 */
    @Resource
    private FlowUtils flowUtils ;

    public String sendEmailVerifyCode(String type, String email, String ip) {
        /* TODO: Written by - Han Yongding 2023/09/18 加锁，防止同一时间被多次调用(会排队), 用intern做锁是因为可以提升性能 */
        synchronized (ip.intern()) {
            /* TODO: Written by - Han Yongding 2023/09/18 如果没通过验证 */
            if (!this.verifyLimit(ip)) {
                return "请求频繁，请稍后再试!" ;
            }
            /* TODO: Written by - Han Yongding 2023/09/18 生成一个随机的六位数验证码，*/
            String code = RandomStringUtil.getRandomSixDigitCode() ;
            /* TODO: Written by - Han Yongding 2023/09/18 存储创建好的随机验证码 */
            Map<String, Object> data = Map.of("type", type, "email", email, "code", code) ;
            /* TODO: Written by - Han Yongding 2023/09/18 将验证码放入注册好的消息队列中 */
            amqpTemplate.convertAndSend("mail", data) ;
            /* TODO: Written by - Han Yongding 2023/09/18 用于校验使用，将验证码放入redis中, 过期时间3分钟 */
            stringRedisTemplate.opsForValue()
                    .set(Const.VERIFY_EMAIL_DATA + email, String.valueOf(code), 3, TimeUnit.MINUTES) ;
            /* TODO: Written by - Han Yongding 2023/09/18 redis简单的限流 */
            return null ;
        }

    }
    /** TODO: Written by - Han Yongding 2023/09/18 限流前检查 */
    private boolean verifyLimit (String ip){
        String key = Const.VERIFY_EMAIL_LIMIT + ip;
        /* TODO: Written by - Han Yongding 2023/09/18 限制60秒 */
        return flowUtils.limitOnceCheck(key, 60);
    }
}
