package priv.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import priv.backend.domain.dto.Role;
import priv.backend.domain.dto.User;
import priv.backend.domain.vo.request.RestRoleVO;
import priv.backend.domain.vo.response.RespRoleVO;
import priv.backend.domain.vo.response.RespUserRoleNoIdVO;
import priv.backend.mapper.UserMapper;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 作用角色业务层
 * @CreateDate :  2024-02-03 22:03
 */
public interface RoleService {
    /* TODO: Written by - Han Yongding 2024/01/28 获取所有角色 */
    Object getRoles(int pageNum, int pageSize, boolean isItPaginated) ;

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

    /* TODO: Written by - Han Yongding 2024/02/15 删除角色 */
    String deleteRoleById(String roleId) ;

    /* TODO: Written by - Han Yongding 2024/02/17 插入中间表 */
    boolean insertRoleOfPermissionByRoleId(String roleId, List<String> permissionList) ;

    /* TODO: Written by - Han Yongding 2024/02/17 删除角色中间表权限 */
    boolean deleteRoleOfPermissionByRoleId(String roleId) ;

    /* TODO: Written by - Han Yongding 2024/02/16 删除中间表数据 */
    boolean deleteRolePermissionByRoleId(String roleId) ;

    /* TODO: Written by - Han Yongding 2024/02/16 根据ID获取对应角色 */
    RespUserRoleNoIdVO getRoleById(String roleId) ;
}
