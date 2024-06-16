package priv.backend.listener.kafka;

import jakarta.annotation.Resource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import priv.backend.domain.dto.RequestLog;
import priv.backend.service.impl.RequestLogServiceImpl;
import priv.backend.util.LogUtils;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 请求日志消费
 * @CreateDate :  2024-06-16 22:26
 */
@Component
public class RequestLogListener {

    /* TODO: Written by - Han Yongding 2024/06/16 注入请求日志业务层实现类 */
    @Resource
    private RequestLogServiceImpl service ;

    @KafkaListener(topics = "request_logs", groupId = "request-log-group")

    public void listen(RequestLog log) {
        LogUtils.info(this.getClass(),"kafka-接收到请求日志");
        if (service.save(log)) {
            LogUtils.info(this.getClass(), "kafka-请求日志已记录") ;
        } else {
            LogUtils.info(this.getClass(), "kafka-请求日志记录失败") ;
        }
    }
}
