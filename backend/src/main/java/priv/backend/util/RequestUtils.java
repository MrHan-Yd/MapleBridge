package priv.backend.util;

import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.stereotype.Component;
import priv.backend.domain.Request;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 请求工具类
 * @CreateDate :  2024-06-08 15:25
 */
@Component
public class RequestUtils {

    /**
     * 获取请求详情
     * @param request 请求对象
     */
    public Request getRequestDetails(HttpServletRequest request) {
        Request data = new Request();
        /* 获取请求头信息 */
        String header = request.getHeader("User-Agent");
        data.setUserAgent(header);
        /* 获取请求参数 */
        UserAgent userAgent = UserAgent.parseUserAgentString(header);
        /* 获取设备类型 */
        String deviceType = userAgent.getOperatingSystem().getDeviceType().toString();
        data.setDeviceType("UNKNOWN".equals(deviceType) ? "未知" : deviceType);
        /* 获取操作系统名称 */
        String osName = userAgent.getOperatingSystem().getName();
        data.setOsName("Unknown".equals(osName) ? "未知" : osName);
        /* 获取请求IP地址 */
        String ip = getIpAddress(request);
        data.setIp(ip);
        /* TODO: Written by - Han Yongding 2024/06/08 获取浏览器类型 */
        String browserType = userAgent.getBrowser().getName();
        data.setBrowserType("Unknown".equals(browserType) ? "未知" : browserType);
        String browserVersion;
        if (header.contains("postman") || header.contains("Apifox")) {
            browserVersion = "未知";
        } else {
            /* TODO: Written by - Han Yongding 2024/06/08 获取浏览器版本 */
            String string = userAgent.getBrowser().getVersion(header).toString();
            browserVersion = string == null ? "未知" : string;
        }

        data.setBrowserVersion(browserVersion);

        return data ;
    }

    /**
     * 获取请求IP地址
     * @param request 请求对象
     * @return ip地址
     */
    public String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
            LogUtils.info(this.getClass(), "X-Real-IP:" + ip);
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("http_client_ip");
            LogUtils.info(this.getClass(), "http_client_ip:" + ip);
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            LogUtils.info(this.getClass(), "getRemoteAddr:" + ip);
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            LogUtils.info(this.getClass(), "Proxy-Client-IP:" + ip);
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            LogUtils.info(this.getClass(), "WL-Proxy-Client-IP:" + ip);
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            LogUtils.info(this.getClass(), "HTTP_X_FORWARDED_FOR:" + ip);
        }
        /* 如果是多级代理，那么取第一个ip为客户ip */
        if (ip != null && ip.contains(",")) {
            ip = ip.substring(ip.lastIndexOf(",") + 1).trim();
            LogUtils.info(this.getClass(), "ip:" + ip);
        }

        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "0.0.0.0";
        }
        return ip;
    }
}
