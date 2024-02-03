package priv.backend.domain.vo.request;

import lombok.Data;
import priv.backend.domain.BaseData;
import priv.backend.domain.dto.Permission;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 接收前端新增角色实体类
 * @CreateDate :  2024-01-29 13:57
 */
@Data
public class RestRoleVO implements BaseData {
    private String roleId ;
    private String roleName ;
    private String roleNameCn ;
    private String statusId ;
    private List<Object> permissionList ;
}
