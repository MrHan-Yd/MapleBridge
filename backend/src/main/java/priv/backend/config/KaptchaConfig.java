package priv.backend.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 谷歌验证码配置
 * @CreateDate :  2024-02-18 15:12
 */
@Configuration
public class KaptchaConfig {

    /** TODO: Written by - Han Yongding 2024/02/18 英文验证码Bean配置 */
    @Bean(name = "captchaProducer")
    public DefaultKaptcha getKaptchaBean(){
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha() ;
        Properties properties = new Properties() ;
        properties.setProperty("kaptcha.border", "yes") ;
        properties.setProperty("kaptcha.border.color", "220,223,230") ;
        properties.setProperty("kaptcha.textproducer.font.color", "blue") ;
        properties.setProperty("kaptcha.image.width", "100") ;
        properties.setProperty("kaptcha.image.height", "50") ;
        properties.setProperty("kaptcha.textproducer.font.size", "28") ;
        properties.setProperty("kaptcha.session.key", "kaptchaCode") ;
        properties.setProperty("kaptcha.textproducer.char.spac", "35") ;
        properties.setProperty("kaptcha.textproducer.char.length", "4") ;
        properties.setProperty("kaptcha.textproducer.font.names", "Arial,Courier,DejaVu Sans") ;
        properties.setProperty("kaptcha.noise.color", "white") ;
        Config config = new Config(properties) ;
        defaultKaptcha.setConfig(config) ;
        return defaultKaptcha ;
    }

    /** TODO: Written by - Han Yongding 2024/02/18 算数验证码Bean配置 */
    @Bean(name = "captchaProducerMath")
    public DefaultKaptcha getKaptchaBeanMath(){
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha() ;
        Properties properties = new Properties() ;
        properties.setProperty("kaptcha.border", "yes") ;
        properties.setProperty("kaptcha.border.color", "220,223,230") ;
        properties.setProperty("kaptcha.textproducer.font.color", "blue") ;
        properties.setProperty("kaptcha.image.width", "100") ;
        properties.setProperty("kaptcha.image.height", "50") ;
        properties.setProperty("kaptcha.textproducer.font.size", "28") ;
        properties.setProperty("kaptcha.session.key", "kaptchaCodeMath") ;
        properties.setProperty("kaptcha.textproducer.impl", "priv.backend.util.KaptchaTextUtils") ;
        properties.setProperty("kaptcha.textproducer.char.spac", "5") ;
        properties.setProperty("kaptcha.textproducer.char.length", "4") ;
        properties.setProperty("kaptcha.textproducer.font.names", "Arial,Courier,DejaVu Sans") ;
        properties.setProperty("kaptcha.noise.color", "black") ;
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise") ;
        properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.ShadowGimpy") ;
        Config config = new Config(properties) ;
        defaultKaptcha.setConfig(config) ;
        return defaultKaptcha ;
    }
    
    @Bean("defaultKaptcha")
    public DefaultKaptcha getDefaultKaptcha(){
        return new DefaultKaptcha();
    }
}
