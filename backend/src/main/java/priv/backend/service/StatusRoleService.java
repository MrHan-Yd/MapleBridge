package priv.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import priv.backend.domain.vo.request.RestRoleStateVO;
import priv.backend.domain.vo.request.RestStatusRoleVO;
import priv.backend.domain.vo.response.RespStatusRoleVO;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 角色状态业务层
 * @CreateDate :  2024-02-03 22:09
 */
public interface StatusRoleService {

    /* TODO: Written by - Han Yongding 2024/01/22 新增角色状态 */
    String insertStatusRole(RestStatusRoleVO vo) ;

    /* TODO: Written by - Han Yongding 2024/01/23 查询所有角色状态 */
    Page<RespStatusRoleVO> getAllStatusRoles(int pageNum, int pageSize) ;

    /* TODO: Written by - Han Yongding 2024/01/24 根据ID修改角色状态 */
    String updateStatusRoleStateById(RestRoleStateVO vo) ;
}
