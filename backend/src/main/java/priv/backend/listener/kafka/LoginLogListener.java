package priv.backend.listener.kafka;

import jakarta.annotation.Resource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import priv.backend.domain.dto.LoginLog;
import priv.backend.service.impl.LoginLogServiceImpl;
import priv.backend.util.LogUtils;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 登录日志消费
 * @CreateDate :  2024-06-16 22:16
 */
@Component
public class LoginLogListener {

    // 注入LoginLogServiceImpl登录日志业务层实现类
    @Resource
    private LoginLogServiceImpl service;

    @KafkaListener(topics = "login_logs", groupId = "login-log-group")
    public void LoginLogConsumer(LoginLog loginLog) {
        LogUtils.info(this.getClass(),"kafka-接收到登录日志");
        // 保存登录日志到数据库
        if (service.saveLoginLog(loginLog)) {
            LogUtils.info(this.getClass(), "kafka-登录日志已记录");
        } else {
            LogUtils.error(this.getClass(), "kafka-登录日志记录失败");
        }
    }
}
