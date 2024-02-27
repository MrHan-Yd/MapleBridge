package priv.backend.listener;

import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 邮件队列监听器，用于消费邮件
 * @CreateDate :  2023-08-13 20:26
 */
@Component
@RabbitListener(queues = "mail")
public class MailQueueListener {
    @Resource
    JavaMailSender sender ;

    @Value("${spring.mail.username}")
    String userName ;

    /* TODO: Written by - Han Yongding 2023/08/13 发送邮件 */
    @RabbitHandler
    public void sendMailMessage(Map<String, Object> data) {
        String email = (String)data.get("email") ;
        String code = (String)data.get("code") ;
        String type = (String)data.get("type") ;
        SimpleMailMessage message = switch (type) {
            case "register" ->
                    createMessage("欢迎注册我们的网站",
                            "您的邮件注册验证码为：" + code + "，有效时间3分钟，为了保障您的安全，请勿向他人泄露验证码。", email) ;
            case "reset" ->
                    createMessage("您的密码重置邮件",
                            "您好，您正在进行重置密码操作，验证码：" + code + ",有效时间3分钟，如非本人操作，请无视。", email) ;
            default -> null ;
        } ;
        /* TODO: Written by - Han Yongding 2023/08/13 如果没有对应的类型 */
        if(message == null) {
            return ;
        }
        /* TODO: Written by - Han Yongding 2023/08/13 发送邮件 */
        sender.send(message) ;
    }

    /* TODO: Written by - Han Yongding 2023/08/13 根据邮件类型生成对应的邮件结构 */
    private SimpleMailMessage createMessage(String title, String content, String email) {
        SimpleMailMessage message = new SimpleMailMessage() ;
        /* TODO: Written by - Han Yongding 2023/08/13 邮件的主题 */
        message.setSubject(title) ;
        /* TODO: Written by - Han Yongding 2023/08/13 邮件的内容 */
        message.setText(content) ;
        /* TODO: Written by - Han Yongding 2023/08/13 邮件接收方 */
        message.setTo(email) ;
        /* TODO: Written by - Han Yongding 2023/08/13 我的邮件地址 */
        message.setFrom(userName) ;
        return message ;
    }
}
