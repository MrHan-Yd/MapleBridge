package priv.backend.service;

import priv.backend.domain.vo.request.RestPermissionVO;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 权限业务层
 * @CreateDate :  2024-02-03 21:58
 */
public interface PermissionService {
    /* TODO: Written by - Han Yongding 2024/01/27 查询权限表 */
    Object getAllPermission(int pageNum, int pageSize, boolean isItPaginated) ;

    /* TODO: Written by - Han Yongding 2024/01/27 新增权限 */
    String insertPermission(RestPermissionVO vo) ;

    /* TODO: Written by - Han Yongding 2024/01/27 修改权限 */
    String updatePermission(RestPermissionVO vo) ;

    /* TODO: Written by - Han Yongding 2024/01/27 删除权限(伪删除) */
    String deletePermission(String permissionId) ;
}
