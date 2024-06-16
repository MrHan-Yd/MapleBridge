package priv.backend.annotations.aspect;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import priv.backend.domain.IpInformation;
import priv.backend.domain.Request;
import priv.backend.domain.dto.RequestLog;
import priv.backend.enumeration.KafkaTopicEnum;
import priv.backend.util.*;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 请求日志切面
 * @CreateDate :  2024-06-16 15:49
 */
@Aspect
@Component
public class RequestLogAop {

    /* TODO: Written by - Han Yongding 2024/06/16 请求工具类 */
    @Resource
    private RequestUtils utils ;

    /* TODO: Written by - Han Yongding 2024/06/16 解析IP工具类 */
    @Resource
    private Ip2RegionUtils ip2RegionUtils ;

    /* TODO: Written by - Han Yongding 2024/06/16 注入Kafka生产工具类 */
    @Resource
    private KafkaProducerUtils kafkaProducerUtils ;


    /* TODO: Written by - Han Yongding 2024/06/16 切点 */
    @Pointcut("execution(* priv.backend.controller..*.*(..))")
    public void requestLogPointcut() {

    }

    @Before("requestLogPointcut()")
    public void doAfter() {
        // 记录开始时间
        TimeUtils.start();
    }

    @AfterReturning(pointcut = "requestLogPointcut()", returning = "retObject")
    public void doAfterReturning(JoinPoint joinPoint, Object retObject) {
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        assert attributes != null ;
        HttpServletRequest request = attributes.getRequest() ;
        /* TODO: Written by - Han Yongding 2024/06/16 请求信息解析 */
        Request requestDetails = utils.getRequestDetails(request);
        RequestLog requestLog = ip2RegionUtils.getIpInformation(ip2RegionUtils.getIpPosition(requestDetails.getIp())).asViewObject(RequestLog.class, v -> {
            v.setUrl(request.getRequestURI());
            v.setMethod(request.getMethod());
            v.setIp(requestDetails.getIp());
            v.setDeviceType(requestDetails.getDeviceType());
            v.setOsName(requestDetails.getOsName());
            v.setBrowserType(requestDetails.getBrowserType());
            v.setBrowserVersion(requestDetails.getBrowserVersion());
            /* TODO: Written by - Han Yongding 2024/06/16 请求参数 */
            String parameter = joinPoint.getArgs().length == 0 ? "没有参数" : Arrays.toString(joinPoint.getArgs()) ;
            v.setParameter(parameter);
            /* TODO: Written by - Han Yongding 2024/06/16 程序位置 */
            String position = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
            v.setPosition(position);
            /* TODO: Written by - Han Yongding 2024/06/16 响应值 */
            String responseValue = retObject.toString() ;
            v.setResponseValue(responseValue);
            /* TODO: Written by - Han Yongding 2024/06/16 请求时间 */
            v.setRequestTime(LocalDateTime.now());
        });

        /* TODO: Written by - Han Yongding 2024/06/16 耗时 */
        String time = TimeUtils.end() ;
        requestLog.setTimeConsuming(time);

        /* TODO: Written by - Han Yongding 2024/06/16 传给kafka队列 */
        kafkaProducerUtils.sendMessage(KafkaTopicEnum.REQUEST_LOGS.topic, requestLog) ;
        LogUtils.info(this.getClass(), "登录日志已传给Kafka队列");
        LogUtils.info(this.getClass(), "请求结束，耗时：" + time);
    }
}
