package priv.backend.domain;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 收集请求信息
 * @CreateDate :  2024-06-08 18:28
 */
@Data
public class Request implements BaseData {
    /* 获取用户代理 */
    private String userAgent ;
    /* 获取设备类型 */
    private String deviceType ;
    /* 获取操作系统名称 */
    private String osName ;
    /* 获取请求IP地址 */
    private String ip ;
    /* TODO: Written by - Han Yongding 2024/06/08 获取浏览器类型 */
    private String browserType ;
    /* TODO: Written by - Han Yongding 2024/06/08 获取浏览器版本 */
    private String browserVersion ;
}
