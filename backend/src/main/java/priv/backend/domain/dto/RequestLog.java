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
 * @Description : 登录日志DTO
 * @CreateDate :  2024-06-16 16:03
 */
@Data
@TableName("request_log")
public class RequestLog {
    @TableId(type = IdType.ASSIGN_ID)
    private String id ;
    @TableField("url")
    private String url ;
    @TableField("method")
    private String method ;
    @TableField("ip")
    private String ip ;
    @TableField("position")
    private String position ;
    @TableField("parameter")
    private String parameter ;
    @TableField("response_value")
    private String responseValue ;
    @TableField("time_consuming")
    private String timeConsuming ;
    @TableField("request_time")
    private LocalDateTime requestTime ;
    @TableField("country")
    private String country ;
    @TableField("province")
    private String province ;
    @TableField("region")
    private String region ;
    @TableField("network")
    private String network ;
    @TableField("device_type")
    private String deviceType ;
    @TableField("os_name")
    private String osName ;
    @TableField("browser_type")
    private String browserType ;
    @TableField("browser_version")
    private String browserVersion ;
}
