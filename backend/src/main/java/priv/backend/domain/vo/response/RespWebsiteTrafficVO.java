package priv.backend.domain.vo.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 响应前端网站流量数据对象
 * @CreateDate :  2024-06-09 16:35
 */
@Data
public class RespWebsiteTrafficVO {
    private String pageUrl ;
    /* 获取请求参数 */
    private String userAgent ;
    /* 获取设备类型 */
    private String deviceType ;
    /* 获取操作系统名称 */
    private String osName ;
    /* 获取请求IP地址 */
    private String ip ;
    /* 获取浏览器类型 */
    private String browserType ;
    /* 获取浏览器版本 */
    private String browserVersion ;
    /* 获取访问时间 */
    private LocalDateTime timestamp ;
    /* 国家 */
    private String country ;
    /* 地区 */
    private String region ;
    /* 网络类型 */
    private String network ;
}
