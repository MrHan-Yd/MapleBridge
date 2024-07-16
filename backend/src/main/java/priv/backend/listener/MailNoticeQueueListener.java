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
 * @Description : 邮件队列监听器，用于消费通知邮件
 * @CreateDate :  2024-03-28 21:58
 */
@Component
@Slf4j
@RabbitListener(queues = "notice")
public class MailNoticeQueueListener {
    @Resource
    JavaMailSender sender ;

    @Value("${spring.mail.username}")
    String userName ;

    /* TODO: Written by - Han Yongding 2024/03/28 发送邮件 */
    @RabbitHandler
    public void sendMailMessage(Map<String, Object> data) {
        log.info("正在准备发送邮件") ;
        String email = (String)data.get("email") ;
        String type = (String)data.get("type") ;
        SimpleMailMessage message = switch (type) {
            case "registerSuccess" ->
                    MailSendUtils.createMessage("校友会",
                            "欢迎注册校友会平台，您的初始密码为：xyh123456，请尽快登录平台修改密码，以确保账号安全",
                            email, userName) ;
            case "slowSQLWarning" ->
                    MailSendUtils.createMessage("校友会管理平台慢SQL监控",
                            "检测到您的SQL执行时间超过5秒，请及时优化您的SQL查询，以提高系统的运行效率，SQL:" + data.get("sql") + "，执行时间：" + data.get("time") + "，执行耗时：" + data.get("timeConsuming"), email, userName);
            default -> null ;
        } ;
        /* TODO: Written by - Han Yongding 2023/08/13 如果没有对应的类型 */
        if(message == null) {
            log.warn("邮件发送异常：[没有对应邮件类型]");
            return ;
        }
        /* TODO: Written by - Han Yongding 2023/08/13 发送邮件 */
        sender.send(message) ;
        log.info("邮件发送完成") ;
    }
}
