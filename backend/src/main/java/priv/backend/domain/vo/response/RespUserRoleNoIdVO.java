package priv.backend.domain.vo.response;

import lombok.Data;
import priv.backend.domain.BaseData;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 用户登录返回的角色信息，不带ID
 * @CreateDate :  2024-02-17 15:55
 */
@Data
public class RespUserRoleNoIdVO implements BaseData {
    private String roleName ;
    private String roleNameCn ;
}
