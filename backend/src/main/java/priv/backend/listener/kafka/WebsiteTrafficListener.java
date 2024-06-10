package priv.backend.listener.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import priv.backend.domain.dto.WebsiteTraffic;
import priv.backend.service.impl.WebsiteTrafficServiceImpl;
import priv.backend.util.LogUtils;
import priv.backend.util.TimeUtils;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 网站流量消费者
 * @CreateDate :  2024-06-09 15:17
 */
@Component
public class WebsiteTrafficListener {

    /* TODO: Written by - Han Yongding 2024/06/09 注入网站流量记录表业务层 */
    private final WebsiteTrafficServiceImpl websiteTrafficService;

    @Autowired
    public WebsiteTrafficListener(WebsiteTrafficServiceImpl websiteTrafficService) {
        this.websiteTrafficService = websiteTrafficService;
    }

    @KafkaListener(topics = "website_traffic_logs", groupId = "website-traffic-group")
    public void websiteTrafficHandler(WebsiteTraffic websiteTraffic) {
        LogUtils.info(this.getClass(), "website-traffic-Kafka" + "——记录平台流量日志任务开始执行");
        /* TODO: Written by - Han Yongding 2024/06/09 记录开始时间 */
        TimeUtils.start() ;

        /* TODO: Written by - Han Yongding 2024/06/09 存入数据库 */
        if(websiteTrafficService.save(websiteTraffic)) {
            LogUtils.info(this.getClass(), "website-traffic-Kafka" + "——日志记录成功");
        } else {
            LogUtils.info(this.getClass(), "website-traffic-Kafka" + "——日志记录失败");
        }

        /* TODO: Written by - Han Yongding 2024/06/09 计算耗时 */
        LogUtils.info(this.getClass(), "website-traffic-Kafka" + "——本次任务耗时" + TimeUtils.end());
        /* TODO: Written by - Han Yongding 2024/06/09 业务结束 */
        LogUtils.info(this.getClass(), "website-traffic-Kafka" + "——记录平台流量日志任务执行完毕");
    }
}
