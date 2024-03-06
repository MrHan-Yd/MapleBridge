package priv.backend.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import priv.backend.domain.RestBean;
import priv.backend.service.system.Impl.CaptchaServiceImpl;
import priv.backend.util.ReturnUtils;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 谷歌验证码控制器
 * @CreateDate :  2024-02-18 15:20
 */
@Slf4j
@RestController
@RequestMapping("/api/auth/")
public class CaptchaController {

    @Resource
    private CaptchaServiceImpl captchaService ;

    /** TODO: Written by - Han Yongding 2024/02/18 获取英文验证码 */
    @GetMapping("captcha-image/{codeId}")
    public void getCaptchaImage(@PathVariable("codeId") String codeId , HttpServletResponse response) {
        try {
            captchaService.getCaptchaImage(codeId, response).flush();
        } catch (Exception e) {
            log.warn("验证码生成失败", e);
        }
    }

    /* TODO: Written by - Han Yongding 2024/02/26 验证验证码 */

    @GetMapping("validate-captcha/{codeId}")
    public RestBean<Void> validateCaptchaImage(@PathVariable("codeId") String codeId) {
        return ReturnUtils.messageHandle(() -> captchaService.validateCaptchaImage(codeId));
    }


    /** TODO: Written by - Han Yongding 2024/02/18 获取算数验证码 */
    @GetMapping("captcha-math/{codeId}")
    public void getCaptchaMath(@PathVariable("codeId") String codeId , HttpServletResponse response) {
        try {
            captchaService.getCaptchaMath(codeId, response).flush();
        } catch (Exception e) {
            log.warn("验证码生成失败", e);
        }
    }
}
