package priv.backend.service.system;


/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 谷歌验证码服务
 * @CreateDate :  2024-02-18 15:26
 */
public interface CaptchaService {
    /* TODO: Written by - Han Yongding 2024/02/18 获取字符验证码 */
    String getCaptchaImage(String codeId) ;

    /* TODO: Written by - Han Yongding 2024/02/18 获取算数验证码 */
    String getCaptchaMath(String codeId) ;

    /* TODO: Written by - Han Yongding 2024/02/26 验证字符验证码 */
    String validateCaptchaImage(String codeId) ;
}
