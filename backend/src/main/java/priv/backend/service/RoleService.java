package priv.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import priv.backend.domain.dto.Role;
import priv.backend.domain.vo.request.RestRoleVO;
import priv.backend.domain.vo.response.RespRoleVO;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 作用角色业务层
 * @CreateDate :  2024-02-03 22:03
 */
public interface RoleService {
    /* TODO: Written by - Han Yongding 2024/01/28 获取所有角色 */
    Page<RespRoleVO> getRoles(int pageNum, int pageSize) ;

    /**
     * 新增角色，并将角色对应权限插入中间表
     *
     * @param vo 包含角色信息和权限列表的VO对象
     * @return 操作结果消息
     */
    String insertRole(RestRoleVO vo) ;

    /**
     * 往数据库中插入角色
     *
     * @param role 角色对象
     * @return 新增的角色ID
     */
    String insertRole(Role role) ;

    /* TODO: Written by - Han Yongding 2024/01/27 修改角色 */
    String updateRole(RestRoleVO vo) ;
}
