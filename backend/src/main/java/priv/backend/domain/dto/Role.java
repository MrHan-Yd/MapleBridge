package priv.backend.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import priv.backend.domain.BaseData;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 角色表DTO
 * @CreateDate :  2024-01-26 13:41
 */
@Data
@TableName("role")
public class Role implements BaseData {
    @TableId(type = IdType.ASSIGN_ID)
    private String roleId ;
    @TableField("role_name")
    private String roleName ;
    @TableField("role_name_cn")
    private String roleNameCn ;
    @TableField("status_id")
    private String statusId ;
    @TableField("create_id")
    private String createId ;
    @TableField("create_time")
    private Timestamp createTime ;
    @TableField("update_id")
    private String updateId ;
    @TableField("update_time")
    private Timestamp updateTime ;
//    private List<Permission> permissions ;
}
