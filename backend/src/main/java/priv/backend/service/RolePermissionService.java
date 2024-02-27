package priv.backend.service;

import priv.backend.domain.dto.RolePermission;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 角色对应权限表
 * @CreateDate :  2024-02-03 22:01
 */
public interface RolePermissionService {
    /* TODO: Written by - Han Yongding 2024/01/31 批量插入 */
    int batchInsertRolePermission(List<RolePermission> list) ;

    /* TODO: Written by - Han Yongding 2024/02/16 根据角色ID删除中中间表数据 */
    int deleteRolePermissionByRoleId(String roleId) ;
}
