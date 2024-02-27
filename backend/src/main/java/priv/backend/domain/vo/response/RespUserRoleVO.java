package priv.backend.domain.vo.response;

import lombok.Data;
import priv.backend.domain.BaseData;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 用户角色
 * @CreateDate :  2024-02-15 14:51
 */
@Data
public class RespUserRoleVO implements BaseData {
    private String roleId ;
    private String roleName ;
    private String roleNameCn ;
}
