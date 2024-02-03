package priv.backend.domain.vo.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import priv.backend.domain.BaseData;
import priv.backend.domain.dto.Permission;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 响应前端角色表实体类
 * @CreateDate :  2024-01-28 21:04
 */
@Data
public class RespRoleVO implements BaseData {
    private String roleId ;
    private String roleName ;
    private String roleNameCn ;
    private String statusId ;
    private String createId ;
    private Timestamp createTime ;
    private String updateId ;
    private Timestamp updateTime ;
    private List<Permission> permissionList ;
}
