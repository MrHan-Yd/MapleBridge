package priv.backend.filter;

import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import priv.backend.domain.IpInformation;
import priv.backend.domain.Request;
import priv.backend.domain.dto.WebsiteTraffic;
import priv.backend.enumeration.KafkaTopicEnum;
import priv.backend.util.Const;
import priv.backend.util.Ip2RegionUtils;
import priv.backend.util.KafkaProducerUtils;
import priv.backend.util.RequestUtils;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 网站流量请求过滤器
 * @CreateDate :  2024-06-08 19:47
 */
@Component
@Order(Const.ORDER_WEBSITE_TRAFFIC)
public class WebsiteTrafficFilters extends HttpFilter {
    
    /* TODO: Written by - Han Yongding 2024/06/08 注入请求工具类 */
    @Resource
    private RequestUtils requestUtils;

    /* TODO: Written by - Han Yongding 2024/06/09 注入离线IP定位框架工具类 */
    @Resource
    private Ip2RegionUtils ip2RegionUtils;

    /* TODO: Written by - Han Yongding 2024/06/09 注入Kafka工具类 */
    @Resource
    private KafkaProducerUtils kafkaUtils ;

    /* TODO: Written by - Han Yongding 2024/06/08 网站请求流量信息收集 */
    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain) throws IOException, ServletException {
        Request requestDetails = requestUtils.getRequestDetails(request);
        IpInformation ipInformation = ip2RegionUtils.getIpInformation(ip2RegionUtils.getIpPosition(requestDetails.getIp()));
        WebsiteTraffic viewObject = requestDetails.asViewObject(WebsiteTraffic.class, v -> {
            v.setCountry(ipInformation.getCountry());
            v.setPageUrl(request.getRequestURI());
            v.setRegion(ipInformation.getRegion());
            v.setNetwork(ipInformation.getNetwork());
            v.setTimestamp(LocalDateTime.now());
        });

        /* TODO: Written by - Han Yongding 2024/06/09 添加到kafka队列 */
        kafkaUtils.sendMessage(KafkaTopicEnum.WEBSITE_TRAFFIC_LOGS.topic, viewObject) ;
        chain.doFilter(request, response) ;
    }
}
