package priv.backend.mapper;

import priv.backend.config.mybatis.MyBaseMapper;
import priv.backend.domain.dto.Permission;
import priv.backend.domain.dto.RolePermission;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 角色对应权限表DAO
 * @CreateDate :  2024-01-30 16:05
 */
public interface RolePermissionMapper extends MyBaseMapper<RolePermission> {

    /** TODO: Written by - Han Yongding 2024/01/31 根据角色ID查询对应权限 */
    List<Permission> findRolePermissionByRoleId(String roleId) ;
}
