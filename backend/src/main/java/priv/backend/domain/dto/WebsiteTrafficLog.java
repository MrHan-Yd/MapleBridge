package priv.backend.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 网站流量类
 * @CreateDate :  2024-06-08 23:38
 */
@Data
@TableName("website_traffic_log")
public class WebsiteTrafficLog {
    @TableId(type = IdType.ASSIGN_ID)
    private String trafficId ;
    @TableField("page_url")
    private String pageUrl ;
    /* 获取请求参数 */
    @TableField("user_agent")
    private String userAgent ;
    /* 获取设备类型 */
    @TableField("device_type")
    private String deviceType ;
    /* 获取操作系统名称 */
    @TableField("os_name")
    private String osName ;
    /* 获取请求IP地址 */
    @TableField("visitor_ip")
    private String ip ;
    /* 获取浏览器类型 */
    @TableField("browser_type")
    private String browserType ;
    /* 获取浏览器版本 */
    @TableField("browser_version")
    private String browserVersion ;
    /* 获取访问时间 */
    @TableField("timestamp")
    private LocalDateTime timestamp ;
    /* 国家 */
    @TableField("country")
    private String country ;
    /* 地区 */
    @TableField("region")
    private String region ;
    /* 网络类型 */
    @TableField("network")
    private String network ;
}
