package priv.backend.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 发送邮件工具类
 * @CreateDate :  2024-03-28 22:00
 */
@Slf4j
public class MailSendUtils {

    /** TODO: Written by - Han Yongding 2023/08/13 根据邮件类型生成对应的邮件结构 */
    public static SimpleMailMessage createMessage(String title, String content, String email, String userName) {
        log.info("正在为构建邮件内容");
        SimpleMailMessage message = new SimpleMailMessage() ;
        /* TODO: Written by - Han Yongding 2023/08/13 邮件的主题 */
        message.setSubject(title) ;
        /* TODO: Written by - Han Yongding 2023/08/13 邮件的内容 */
        message.setText(content) ;
        /* TODO: Written by - Han Yongding 2023/08/13 邮件接收方 */
        message.setTo(email) ;
        /* TODO: Written by - Han Yongding 2023/08/13 我的邮件地址 */
        message.setFrom(userName) ;
        log.info("邮件内容构建完成");
        return message ;
    }
}
