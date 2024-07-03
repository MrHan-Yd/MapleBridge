package priv.backend.util;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 常用的属性
 * @CreateDate :  2024-01-09 22:16
 */
public class Const {
    /** TODO: Written by - Han Yongding 2023/09/14 Token前缀 */
    public static final String TOKEN_PREFIX = "Bearer" ;

    /* TODO: Written by - Han Yongding 2023/09/14 Redis前缀 */
    /** TODO: Written by - Han Yongding 2023/09/17 Jwt */
    public static final String JWT_BLACK_LIST = "jwt:black:list:" ;
    /** TODO: Written by - Han Yongding 2023/09/17 ip黑名单 */
    public static final String FLOW_LIMIT_COUNTER = "flow:counter:" ;
    public static final String FLOW_LIMIT_BLOCK = "flow:block:" ;

    /** TODO: Written by - Han Yongding 2023/09/18 发送邮件限流Redis前缀 */
    public static final String VERIFY_EMAIL_LIMIT = "verify:email:limit:" ;
    public static final String VERIFY_EMAIL_DATA = "verify:email:data:" ;

    /* TODO: Written by - Han Yongding 2024/02/16 缓存用户信息 */
    public static final String USER_INFORMATION_DATA = "user:information:data:" ;
    /* TODO: Written by - Han Yongding 2024/02/18 验证码RedisKey */
    public static final String CAPTCHA_CODE = "captcha:code:";

    /* TODO: Written by - Han Yongding 2024/06/08 网站流量统计过滤器优先级 */
    public static final int ORDER_WEBSITE_TRAFFIC = -101 ;

    /** TODO: Written by - Han Yongding 2023/08/22 限流过滤器优先级 */
    public static final int ORDER_LIMIT = -102 ;

    /* TODO: Written by - Han Yongding 2024/06/30 敏感词过滤器优先级 */
    public static final int ORDER_SENSITIVE_WORDS = -103 ;

    /** TODO: Written by - Han Yongding 2023/08/22 跨域过滤器优先级 */
    public static final int ORDER_CORS = -104 ;

    /* TODO: Written by - Han Yongding 2024/05/28 redis数据key */
    public static final String REDIS_DATA_KEY = "data:" ;

}
