package priv.backend.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import priv.backend.domain.BaseData;

import java.time.LocalDateTime;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 登录日志
 * @CreateDate :  2024-06-15 16:31
 */
@Data
@TableName("login_log")
public class LoginLog implements BaseData {
    @TableId(type = IdType.ASSIGN_ID)
    private String id ;
    @TableField("username")
    private String username ;
    @TableField("login_time")
    private LocalDateTime loginTime ;
    @TableField("login_device")
    private String loginDevice ;
    @TableField("login_result")
    private String loginResult ;
    @TableField("ip")
    private String ip ;
    @TableField("country")
    private String country ;
    @TableField("province")
    private String province ;
    @TableField("region")
    private String region ;
    @TableField("network")
    private String network ;
}
