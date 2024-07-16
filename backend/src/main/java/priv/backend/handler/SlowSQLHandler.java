package priv.backend.handler;

import jakarta.annotation.Resource;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import priv.backend.util.TimeUtils;

import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 慢SQL拦截处理器
 * @CreateDate :  2024-07-15 14:50
 */
@Component
@Intercepts(@Signature(
        type = StatementHandler.class,
        method = "query",
        args = {Statement.class, ResultHandler.class}
))
public class SlowSQLHandler implements Interceptor {

    @Value("${spring.mail.username}")
    private String adminEmail;

    /* TODO: Written by - Han Yongding 2024/07/15 rabbitMQ队列 */
    @Resource
    private AmqpTemplate amqpTemplate;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        /* TODO: Written by - Han Yongding 2024/07/15 记录开始时间 */
        TimeUtils.start();
        /* TODO: Written by - Han Yongding 2024/07/15 获取SQL */

        String sql = ((StatementHandler) invocation.getTarget()).getBoundSql().getSql();
        /* TODO: Written by - Han Yongding 2024/07/15 执行数据库操作 */
        Object result = invocation.proceed();
        /* TODO: Written by - Han Yongding 2024/07/15 如果是慢SQL */
        if (TimeUtils.getCostTime() > 5000) {
            /* TODO: Written by - Han Yongding 2024/07/15 通知邮件通知管理员 */
            Map<String, Object> data = new HashMap<>();
            // 定义日期时间格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分ss秒");

            // 格式化日期时间
            String formattedDateTime = TimeUtils.getLocalDateTime().format(formatter);
            data.put("type", "slowSQLWarning");
            data.put("email", adminEmail);
            data.put("sql", sql);
            data.put("time", formattedDateTime);
            data.put("timeConsuming", TimeUtils.end());
            amqpTemplate.convertAndSend("notice", data);
        }


        /* TODO: Written by - Han Yongding 2024/07/15 */
        return result;
    }
}
