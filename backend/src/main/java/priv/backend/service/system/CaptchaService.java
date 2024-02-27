package priv.backend.service.system;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import priv.backend.domain.RestBean;

import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 谷歌验证码服务
 * @CreateDate :  2024-02-18 15:26
 */
public interface CaptchaService {
    /* TODO: Written by - Han Yongding 2024/02/18 获取字符验证码 */
    ServletOutputStream getCaptchaImage(String codeId, HttpServletResponse response) ;

    /* TODO: Written by - Han Yongding 2024/02/18 获取算数验证码 */
    ServletOutputStream getCaptchaMath(String codeId, HttpServletResponse response) ;

    /* TODO: Written by - Han Yongding 2024/02/26 验证字符验证码 */
    String validateCaptchaImage(String codeId) ;
}
