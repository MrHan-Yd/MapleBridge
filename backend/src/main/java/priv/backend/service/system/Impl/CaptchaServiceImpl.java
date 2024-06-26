package priv.backend.service.system.Impl;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import priv.backend.service.system.CaptchaService;
import priv.backend.util.Const;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 谷歌验证码服务实现类
 * @CreateDate :  2024-02-18 15:29
 */
@Slf4j
@Service
public class CaptchaServiceImpl implements CaptchaService {

    /* TODO: Written by - Han Yongding 2024/02/18 字符验证码注入 */
    private final DefaultKaptcha captchaProducer;
    /* TODO: Written by - Han Yongding 2024/02/18 算数验证码注入 */
    private final DefaultKaptcha captchaProducerMath;

    /** TODO: Written by - Han Yongding 2024/02/18 注入Redis */
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取验证码
     * @param captchaProducer 英文验证码Bean
     * @param captchaProducerMath 算数验证码Bean
     */
    @Autowired
    public CaptchaServiceImpl(@Qualifier("captchaProducer") DefaultKaptcha captchaProducer,
                              @Qualifier("captchaProducerMath") DefaultKaptcha captchaProducerMath) {
        this.captchaProducer = captchaProducer;
        this.captchaProducerMath = captchaProducerMath;
    }

    /** TODO: Written by - Han Yongding 2024/02/18 获取字符验证码 */
    @Override
    public String getCaptchaImage(String codeId) {
        // 获取英文验证码文本
        String captchaText = captchaProducer.createText();
        // 将验证码文本存储在Redis中
        stringRedisTemplate.opsForValue().set(Const.CAPTCHA_CODE + codeId, captchaText, 30, TimeUnit.SECONDS);

        // 获取验证码图片
        BufferedImage bi = captchaProducer.createImage(captchaText);

        // 将验证码图片转换为Base64编码的字符串
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, "jpg", baos);
        } catch (IOException e) {
            log.warn("验证码图片转换失败", e);
            return null;
        }

        byte[] imageBytes = baos.toByteArray();

        return Base64.getEncoder().encodeToString(imageBytes);
    }

    /** TODO: Written by - Han Yongding 2024/02/18 获取算数验证码 */
    @Override
    public String getCaptchaMath(String codeId) {
        // 获取数学验证码文本
        String captchaText = captchaProducerMath.createText();

        /* TODO: Written by - Han Yongding 2024/02/18 截取等号之前的字符 */
        String capStr = captchaText.substring(0, captchaText.lastIndexOf("@"));
        /* TODO: Written by - Han Yongding 2024/02/18 算数结果 */
        String code = captchaText.substring(captchaText.lastIndexOf("@") + 1);
        /* TODO: Written by - Han Yongding 2024/02/18 获取截取后的BufferedImage对象 */
        BufferedImage bi = captchaProducerMath.createImage(capStr);

        // 将验证码结果存储在Redis中
        stringRedisTemplate.opsForValue().set(Const.CAPTCHA_CODE + codeId, code, 30, TimeUnit.SECONDS);

        // 将图片数据转换为Base64编码的字符串
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, "jpg", baos);
        } catch (IOException e) {
            log.warn("验证码图片转换失败", e);
            return null;
        }

        byte[] imageBytes = baos.toByteArray();

        return Base64.getEncoder().encodeToString(imageBytes);
    }

    /** TODO: Written by - Han Yongding 2024/02/26 验证字符验证码 */
    @Override
    public String validateCaptchaImage(String codeId) {
        String[] split = codeId.split("-_-");

        /* TODO: Written by - Han Yongding 2024/02/26 验证码错误 */
        if (!Objects.equals(stringRedisTemplate.opsForValue().get(Const.CAPTCHA_CODE + split[1]), split[0])) {
            return "验证码错误" ;
        }

        /* TODO: Written by - Han Yongding 2024/02/26 验证码正确 */
        return null ;
    }

}
