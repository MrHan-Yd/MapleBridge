package priv.backend.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 角色表
 * @CreateDate :  2024-01-26 13:41
 */
@Data
@TableName("role")
public class Role {
    @TableId(type = IdType.ASSIGN_ID)
    private String roleId ;
    @TableField("role_name")
    private String roleName ;
    @TableField("status_id")
    private String statusId ;
    @TableField("role_name_cn")
    private String roleNameCn ;
    @TableField("create_id")
    private String createId ;
    @TableField("create_time")
    private Timestamp createTime ;
    @TableField("update_id")
    private String updateId ;
    @TableField("update_time")
    private Timestamp updateTime ;
}
