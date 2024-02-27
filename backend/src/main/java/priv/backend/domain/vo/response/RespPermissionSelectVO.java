package priv.backend.domain.vo.response;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 响应前端权限选择列表
 * @CreateDate :  2024-02-15 17:46
 */
@Data
public class RespPermissionSelectVO {
    private String permissionId ;
    private String permissionName ;
    private String permissionUrl ;
}
