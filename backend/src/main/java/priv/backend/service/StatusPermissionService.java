package priv.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import priv.backend.domain.vo.request.RestPermissionStateVO;
import priv.backend.domain.vo.request.RestStatusPermissionVO;
import priv.backend.domain.vo.response.RespStatusPermissionVO;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 权限状态业务
 * @CreateDate :  2024-02-03 22:07
 */
public interface StatusPermissionService {

    /* TODO: Written by - Han Yongding 2024/01/22 新增权限状态 */
    String insertStatusPermission(RestStatusPermissionVO vo) ;

    /* TODO: Written by - Han Yongding 2024/01/26 查询所有权限状态 */
    Page<RespStatusPermissionVO> getAllStatusRoles(int pageNum, int pageSize) ;

    /* TODO: Written by - Han Yongding 2024/01/26 根据ID修改权限状态 */
    String updatePermissionStateById(RestPermissionStateVO vo) ;
}
