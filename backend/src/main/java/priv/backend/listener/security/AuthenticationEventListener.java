package priv.backend.listener.security;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import priv.backend.domain.Request;
import priv.backend.domain.dto.LoginLog;
import priv.backend.enumeration.KafkaTopicEnum;
import priv.backend.service.impl.LoginLogServiceImpl;
import priv.backend.util.Ip2RegionUtils;
import priv.backend.util.KafkaProducerUtils;
import priv.backend.util.LogUtils;
import priv.backend.util.RequestUtils;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : Security事件监听器（做登录日志记录）
 * @CreateDate : 2024-06-15 17:19
 */
@Component
public class AuthenticationEventListener implements ApplicationListener<AbstractAuthenticationEvent> {
    // 注入RequestUtils工具类
    @Resource
    private RequestUtils requestUtils;

    // 注入Ip2RegionUtils工具类
    @Resource
    private Ip2RegionUtils ip2RegionUtils;

    /* TODO: Written by - Han Yongding 2024/06/16 注入kafka工具类 */
    @Resource
    private KafkaProducerUtils kafkaProducerUtils ;


    /**
     * 处理认证事件
     * @param event 认证事件
     */
    @Override
    public void onApplicationEvent(AbstractAuthenticationEvent event) {
        // 如果事件是认证成功事件，则处理成功事件
        if (event instanceof AuthenticationSuccessEvent) {
            handleSuccessEvent((AuthenticationSuccessEvent) event);
        }
        // 如果事件是认证失败事件，则处理失败事件
        else if (event instanceof AuthenticationFailureBadCredentialsEvent) {
            handleFailureEvent((AuthenticationFailureBadCredentialsEvent) event);
        }
    }

    /**
     * 处理认证成功事件
     * @param event 认证成功事件
     */
    private void handleSuccessEvent(AuthenticationSuccessEvent event) {
        // 获取认证信息
        Authentication authentication = event.getAuthentication();
        // 获取用户名
        String username = authentication.getName();
        // 获取HttpServletRequest对象
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        // 保存登录成功日志
        save(request, username, "登录成功");
    }

    /**
     * 处理认证失败事件
     * @param event 认证失败事件
     */
    private void handleFailureEvent(AuthenticationFailureBadCredentialsEvent event) {
        // 获取HttpServletRequest对象
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        // 获取用户名
        String username = request.getParameter("username");
        // 获取异常信息
        String errorMessage = event.getException().getMessage();
        // 保存登录失败日志
        save(request, username, errorMessage);
    }

    /**
     * 保存登录日志
     * @param request HttpServletRequest对象
     * @param username 用户名
     * @param result 登录结果
     */
    private void save(HttpServletRequest request, String username, String result) {
        // 获取请求详细信息
        Request requestVO = requestUtils.getRequestDetails(request);
        // 获取IP信息并转换为LoginLog对象
        LoginLog loginLogVO = ip2RegionUtils.getIpInformation(ip2RegionUtils
                        .getIpPosition(requestVO.getIp()))
                .asViewObject(LoginLog.class, v -> {
                    v.setUsername(username);
                    v.setLoginTime(LocalDateTime.now());
                    v.setLoginDevice(requestVO.getDeviceType());
                    v.setLoginResult(result);
                    v.setIp(requestVO.getIp());
                });

        kafkaProducerUtils.sendMessage(KafkaTopicEnum.LOGIN_LOGS.topic, loginLogVO);
        LogUtils.info(this.getClass(), "已传入Kafka队列");
    }
}
