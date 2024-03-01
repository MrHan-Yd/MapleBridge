package priv.backend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import priv.backend.domain.dto.Role;
import priv.backend.domain.dto.RolePermission;
import priv.backend.domain.vo.request.RestRoleVO;
import priv.backend.domain.vo.response.RespUserRoleNoIdVO;
import priv.backend.mapper.RoleMapper;
import priv.backend.service.RoleService;
import priv.backend.util.CurrentUtils;

import java.util.List;

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
    private RoleMapper mapper;

    /* TODO: Written by - Han Yongding 2024/01/30 注入角色对应权限中间表业务层 */
    @Resource
    private RolePermissionServiceImpl rolePermissionService;

    /* TODO: Written by - Han Yongding 2024/01/28 获取所有角色 */
    @Override
    public Object getRoles(int pageNum, int pageSize, boolean isItPaginated) {
        /* TODO: Written by - Han Yongding 2024/02/15 数据展示，分页 */
        if (isItPaginated) {
            Page<Role> page = new Page<>(pageNum, pageSize);
            return mapper.getRole(page);
        }
        /* TODO: Written by - Han Yongding 2024/02/15 数据选择，不分页 */
        return mapper.getRoleSelect();
    }

    /* TODO: Written by - Han Yongding 2024/01/31 编程式事务：新增角色事务，确保新增角色和中间表为原子操作 */
    @Resource
    private TransactionTemplate insertRolePermissionTemplate;

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
                if (roleId != null) {
                    /* TODO: Written by - Han Yongding 2024/01/30 将角色对应权限插入中间表 */
                    int rs = rolePermissionService.batchInsertRolePermission(
                            vo.getPermissionIdList()
                                    .stream()
                                    .map(s -> {
                                        RolePermission rolePermission = new RolePermission();
                                        rolePermission.setRoleId(roleId);
                                        rolePermission.setPermissionId(s);
                                        return rolePermission;
                                    }).toList());
                    if (rs <= 0) {
                        /* TODO: Written by - Han Yongding 2024/01/31 手动回滚事务 */
                        status.setRollbackOnly();
                        return "中间表插入失败，请稍后重试";
                    }
                } else {
                    /* TODO: Written by - Han Yongding 2024/01/31 手动回滚事务 */
                    status.setRollbackOnly();
                    return "新增失败，请稍后重试";
                }
                /* TODO: Written by - Han Yongding 2024/01/31 操作成功 */
                return null;
            } catch (Exception e) {
                /* TODO: Written by - Han Yongding 2024/01/31 手动回滚事务 */
                status.setRollbackOnly();
                return "发生异常，请稍候重试";
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
                role.setStatusId("1749402591433838593");
                role.setCreateTime(CurrentUtils.getTheCurrentSystemTime());
                mapper.insert(role);
                /* TODO: Written by - Han Yongding 2024/01/31 返回新增的角色ID */
                return role.getRoleId();
            } catch (Exception e) {
                /* TODO: Written by - Han Yongding 2024/01/31 手动回滚事务 */
                status.setRollbackOnly();
                return null;
            }
        });
    }

    /* TODO: Written by - Han Yongding 2024/02/17 编程式事务：修改删除角色中间表权限 */
    @Resource
    private TransactionTemplate deleteRolePermissionTemplate;

    /* TODO: Written by - Han Yongding 2024/01/27 修改角色 */
    @Override
    public String updateRole(RestRoleVO vo) {
        return deleteRolePermissionTemplate.execute(status -> {
            try {
                if (vo == null) {
                    return "数据不能为空，请稍后重试";
                }
                if (this.insertRoleOfPermissionByRoleId(vo.getRoleId(), vo.getPermissionIdList())) {
                    /* TODO: Written by - Han Yongding 2024/01/27 根据id修改 */
                    if (mapper.updateById(vo.asViewObject(Role.class)) < 1) {
                        return "修改失败，请稍后重试";
                    }
                } else {
                    /* TODO: Written by - Han Yongding 2024/02/17 回滚 */
                    status.setRollbackOnly();
                    return "修改失败，请稍后重试";
                }
                return null;
            } catch (Exception e) {
                /* TODO: Written by - Han Yongding 2024/02/17 回滚 */
                status.setRollbackOnly();
                return "服务器异常，请稍候再试";
            }
            /* TODO: Written by - Han Yongding 2024/02/17 修改成功 */
        });
    }

    /* TODO: Written by - Han Yongding 2024/02/17 插入中间表 */
    @Override
    public boolean insertRoleOfPermissionByRoleId(String roleId, List<String> permissionList) {
        /* TODO: Written by - Han Yongding 2024/02/17 插入数据 */
        return Boolean.TRUE.equals(deleteRolePermissionTemplate.execute(status -> {
            try {
                if (deleteRoleOfPermissionByRoleId(roleId)) {
                    /* TODO: Written by - Han Yongding 2024/02/17 插入数据 */
                    List<RolePermission> list = permissionList
                            .stream()
                            .map(s -> {
                                RolePermission role = new RolePermission();
                                role.setRoleId(roleId);
                                role.setPermissionId(s);
                                return role;
                            }).toList();
                    /* TODO: Written by - Han Yongding 2024/02/17 结果 */
                    return rolePermissionService.batchInsertRolePermission(list) > 0;
                } else {
                    /* TODO: Written by - Han Yongding 2024/02/17 回滚 */
                    status.setRollbackOnly();
                    return "修改失败，请稍后再试";
                }
            } catch (Exception e) {
                /* TODO: Written by - Han Yongding 2024/02/17 回滚 */
                status.setRollbackOnly();
                return "服务器异常，请稍候再试";
            }
        }));
    }

    /* TODO: Written by - Han Yongding 2024/02/17 删除角色中间表权限 */
    @Override
    public boolean deleteRoleOfPermissionByRoleId(String roleId) {
        /* TODO: Written by - Han Yongding 2024/02/16 删除中间表数据 */
        /* TODO: Written by - Han Yongding 2024/02/16 回滚 */
        return Boolean.TRUE.equals(deleteRolePermissionTemplate.execute(status -> {
            try {
                /* TODO: Written by - Han Yongding 2024/02/16 删除中间表数据 */
                return rolePermissionService.deleteRolePermissionByRoleId(roleId) > 0;
            } catch (Exception e) {
                /* TODO: Written by - Han Yongding 2024/02/16 回滚 */
                status.setRollbackOnly();
                return false;
            }
        }));
    }


    /* TODO: Written by - Han Yongding 2024/02/15 删除角色 */

    /* TODO: Written by - Han Yongding 2024/02/16 编程式事务：删除事务 */
    @Resource
    private TransactionTemplate deleteRoleTemplate;

    @Override
    public String deleteRoleById(String roleId) {
        return deleteRoleTemplate.execute(status -> {
            try {
                if (roleId == null) {
                    return "唯一标识为空，请稍后重试";
                }
                /* TODO: Written by - Han Yongding 2024/02/16 先删除中间表 ，删除成功则删除角色表*/
                if (this.deleteRolePermissionByRoleId(roleId)) {
                    /* TODO: Written by - Han Yongding 2024/01/28 伪删除状态 */
                    Role vo = new Role();
                    vo.setRoleId(roleId);
                    vo.setStatusId("1749415120449003521");

                    /* TODO: Written by - Han Yongding 2024/01/27 根据id修改为删除标识 */
                    if (mapper.updateById(vo) < 1) {
                        return "删除失败，请稍后重试";
                    }
                    return null;
                } else {
                    /* TODO: Written by - Han Yongding 2024/02/16 回滚 */
                    status.setRollbackOnly();
                    return "删除中间表数据失败，已回滚，请稍候稍后再试";
                }
            } catch (Exception e) {
                /* TODO: Written by - Han Yongding 2024/02/16 手动回滚事务 */
                status.setRollbackOnly();
                return "删除失败，数据已回滚，请稍后再试";
            }
        });
    }

    /* TODO: Written by - Han Yongding 2024/02/16 删除中间表数据 */
    @Override
    public boolean deleteRolePermissionByRoleId(String roleId) {
        Boolean execute = deleteRoleTemplate.execute(status -> {
            try {
                /* TODO: Written by - Han Yongding 2024/02/16 删除中间表数据 */
                return rolePermissionService.deleteRolePermissionByRoleId(roleId) > 0;
            } catch (Exception e) {
                /* TODO: Written by - Han Yongding 2024/02/16 回滚 */
                status.setRollbackOnly();
                return false;
            }
        });
        /* TODO: Written by - Han Yongding 2024/02/16 没有异常，说明删除成功 */
        return Boolean.TRUE.equals(execute);
    }

    /* TODO: Written by - Han Yongding 2024/02/16 根据ID获取对应角色 */
    @Override
    public RespUserRoleNoIdVO getRoleById(String roleId) {
        return mapper.getRoleById(roleId).asViewObject(RespUserRoleNoIdVO.class);
    }
}
