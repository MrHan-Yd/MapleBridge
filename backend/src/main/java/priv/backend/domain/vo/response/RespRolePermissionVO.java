package priv.backend.domain.vo.response;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 响应前端角色权限类
 * @CreateDate :  2024-01-29 14:01
 */
@Data
public class RespRolePermissionVO {
    private String permissionId ;
    private String permissionName ;
    private String permissionUrl ;
}
