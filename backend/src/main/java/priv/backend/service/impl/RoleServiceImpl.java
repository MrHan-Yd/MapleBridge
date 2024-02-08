package priv.backend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import priv.backend.domain.dto.Role;
import priv.backend.domain.dto.RolePermission;
import priv.backend.domain.vo.request.RestRoleVO;
import priv.backend.domain.vo.response.RespRoleVO;
import priv.backend.mapper.RoleMapper;
import priv.backend.service.RoleService;
import priv.backend.util.CurrentUtils;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 角色业务层实现类
 * @CreateDate :  2024-01-28 20:59
 */
@Service
public class RoleServiceImpl implements RoleService {

    /* TODO: Written by - Han Yongding 2024/01/28 注入角色DAO */
    @Resource
    private RoleMapper mapper ;

    /* TODO: Written by - Han Yongding 2024/01/30 注入角色对应权限中间表业务层 */
    @Resource
    private RolePermissionServiceImpl rolePermissionService ;

    /* TODO: Written by - Han Yongding 2024/01/28 获取所有角色 */
    @Override
    public Page<RespRoleVO> getRoles(int pageNum, int pageSize) {
        Page<Role> page = new Page<>(pageNum, pageSize) ;
        return mapper.getRole(page);
    }

    /* TODO: Written by - Han Yongding 2024/01/31 手动控制事务，编程式事务 */
    @Resource
    private TransactionTemplate insertRolePermissionTemplate ;

    /**
     * 新增角色，并将角色对应权限插入中间表
     *
     * @param vo 包含角色信息和权限列表的VO对象
     * @return 操作结果消息
     */
    @Override
    public String insertRole(RestRoleVO vo) {
        /* TODO: Written by - Han Yongding 2024/01/31 使用事务模板执行数据库操作，确保整个操作是原子的（要么全部成功，要么全部回滚） */
        return insertRolePermissionTemplate.execute(status -> {
            try {
                /* TODO: Written by - Han Yongding 2024/01/30 判断角色是否新增成功 */
                String roleId = insertRole(vo.asViewObject(Role.class));
                if( roleId != null){
                    /* TODO: Written by - Han Yongding 2024/01/30 将角色对应权限插入中间表 */
                    int rs = rolePermissionService.batchInsertRolePermission(
                            vo.getPermissionList()
                                    .stream()
                                    .map(s -> {
                                        RolePermission rolePermission = new RolePermission();
                                        rolePermission.setRoleId(roleId) ;
                                        rolePermission.setPermissionId((String) s);
                                        return rolePermission ;
                                    }).toList());
                    if (rs <= 0) {
                        /* TODO: Written by - Han Yongding 2024/01/31 手动回滚事务 */
                        status.setRollbackOnly();
                        return "中间表插入失败，请稍后重试" ;
                    }
                } else {
                    /* TODO: Written by - Han Yongding 2024/01/31 手动回滚事务 */
                    status.setRollbackOnly();
                    return "新增失败，请稍后重试" ;
                }
                /* TODO: Written by - Han Yongding 2024/01/31 操作成功 */
                return null ;
            } catch (Exception e) {
                /* TODO: Written by - Han Yongding 2024/01/31 手动回滚事务 */
                status.setRollbackOnly();
                return "发生异常，请稍候重试" ;
            }
        });
    }

    /**
     * 往数据库中插入角色
     *
     * @param role 角色对象
     * @return 新增的角色ID
     */
    @Override
    public String insertRole(Role role) {
        /* TODO: Written by - Han Yongding 2024/01/31 使用事务模板执行数据库操作，确保整个操作是原子的（要么全部成功，要么全部回滚） */
        return insertRolePermissionTemplate.execute(status -> {
            try {
                /* TODO: Written by - Han Yongding 2024/01/31 新增角色，状态默认正常 */
                role.setStatusId("1749402591433838593") ;
                role.setCreateId("null") ;
                role.setCreateTime(CurrentUtils.getTheCurrentSystemTime()) ;
                mapper.insert(role) ;
                /* TODO: Written by - Han Yongding 2024/01/31 返回新增的角色ID */
                return role.getRoleId() ;
            } catch (Exception e) {
                /* TODO: Written by - Han Yongding 2024/01/31 手动回滚事务 */
                status.setRollbackOnly() ;
                return null ;
            }
        });
    }

    /* TODO: Written by - Han Yongding 2024/01/27 修改角色 */
    @Override
    public String updateRole(RestRoleVO vo) {
        if (vo == null) {
            return "数据不能为空，请稍后重试" ;
        }

        /* TODO: Written by - Han Yongding 2024/01/27 根据id修改 */
        if (mapper.updateById(vo.asViewObject(Role.class)) < 1) {
            return "修改失败，请稍后重试" ;
        }
        return null ;
    }

}
