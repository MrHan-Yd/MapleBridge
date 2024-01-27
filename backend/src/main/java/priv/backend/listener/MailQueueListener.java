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
 * @Description : 作用
 * @CreateDate :  2024-01-09 22:32
 */
@Component
@RabbitListener(queues = "mail")
public class MailQueueListener {
    /** TODO: Written by - Han Yongding 2023/09/18 注入邮件发送依赖 */
    @Resource
    private JavaMailSender javaMailSender ;

    /** TODO: Written by - Han Yongding 2023/09/18 从配置文件中读取邮件发送人 */
    @Value("${spring.mail.username}")
    private String username ;

    /** TODO: Written by - Han Yongding 2023/09/18 发送邮件 */
    @RabbitHandler
    public void sendMailMessage(Map<String, Object> data) {
        /* TODO: Written by - Han Yongding 2023/09/18 获取邮件类型，接收邮箱，验证码 */
        String type = (String)data.get("type") ;
        String email = (String)data.get("email") ;
        String code = (String)data.get("code") ;
        /* TODO: Written by - Han Yongding 2023/09/18 根据类型发送邮件 */
        SimpleMailMessage message = switch(type) {
            case "register" -> this.createMessage("欢迎注册我们的网站",
                    "您的邮件注册验证码为：" + code + "，有效时间3分钟，为了保障您的安全，请勿向他人泄露验证码。", email) ;
            case "reset" -> this.createMessage("您的密码重置邮件",
                    "您好，您正在进行重置密码操作，验证码：" + code + ",有效时间3分钟，如非本人操作，请无视。", email) ;
            default -> null ;
        } ;
        /* TODO: Written by - Han Yongding 2023/09/18 如果没有对应的类型 */
        if (message == null) {
            return ;
        }
        /* TODO: Written by - Han Yongding 2023/09/18 发送邮件 */
        javaMailSender.send(message) ;
    }

    /** TODO: Written by - Han Yongding 2023/09/18 根据邮件类型生成对应的邮件结构 */
    private SimpleMailMessage createMessage(String title, String content, String email) {
        SimpleMailMessage message = new SimpleMailMessage() ;
        /* TODO: Written by - Han Yongding 2023/09/18 邮件的主题 */
        message.setSubject(title) ;
        /* TODO: Written by - Han Yongding 2023/09/18 邮件的内容 */
        message.setText(content) ;
        /* TODO: Written by - Han Yongding 2023/09/18 邮件接收方 */
        message.setTo(email) ;
        /* TODO: Written by - Han Yongding 2023/09/18 我的邮件地址 */
        message.setFrom(this.username) ;
        return message ;
    }
}
