package priv.backend.listener;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import priv.backend.util.MailSendUtils;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 邮件队列监听器，用于消费验证码邮件
 * @CreateDate :  2023-08-13 20:26
 */
@Slf4j
@Component
@RabbitListener(queues = "mail")
public class MailQueueListener {
    @Resource
    JavaMailSender sender ;

    @Value("${spring.mail.username}")
    String userName ;

    /** TODO: Written by - Han Yongding 2023/08/13 发送邮件 */
    @RabbitHandler
    public void sendMailMessage(Map<String, Object> data) {
        log.info("正在准备发送邮件") ;
        String email = (String)data.get("email") ;
        String code = (String)data.get("code") ;
        String type = (String)data.get("type") ;
        SimpleMailMessage message = switch (type) {
            case "register" ->
                    MailSendUtils.createMessage("欢迎注册校友会",
                            "您的邮件注册验证码为:" + code + "，有效时间3分钟，为了保障您的安全，请勿向他人泄露验证码。",
                            email, userName) ;
            case "reset" ->
                    MailSendUtils.createMessage("您的校友会密码重置邮件",
                            "您好，您正在进行重置密码操作，验证码:" + code + ",有效时间3分钟，如非本人操作，请无视。",
                            email, userName) ;
            case "login" ->
                    MailSendUtils.createMessage("校友会登录验证邮件",
                            "您好，您正在登录校友会平台，验证码:" + code + "有效时间3分钟，如非本人操作，请尽快重置密码。",
                            email, userName) ;
            default -> null ;
        } ;
        /* TODO: Written by - Han Yongding 2023/08/13 如果没有对应的类型 */
        if(message == null) {
            log.warn("邮件发送异常[没有对应邮件类型]");
            return ;
        }
        /* TODO: Written by - Han Yongding 2023/08/13 发送邮件 */
        sender.send(message) ;
        log.info("邮件发送完成") ;
    }

}
