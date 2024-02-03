package priv.backend.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 角色对应权限表
 * @CreateDate :  2024-01-30 16:01
 */
@Data
@TableName("role_permission")
public class RolePermission {
    @TableId(type = IdType.ASSIGN_ID)
    private String id ;
    @TableField("role_id")
    private String roleId ;
    @TableField("permission_id")
    private String permissionId ;
}
