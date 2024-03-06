package priv.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import priv.backend.domain.dto.RolePermission;
import priv.backend.mapper.RolePermissionMapper;
import priv.backend.service.RolePermissionService;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 角色对应权限表实现类
 * @CreateDate :  2024-01-30 16:07
 */
@Service
public class RolePermissionServiceImpl implements RolePermissionService {
    /* TODO: Written by - Han Yongding 2024/01/30 注入角色对应权限表 */
    @Resource
    private RolePermissionMapper mapper ;

    /** TODO: Written by - Han Yongding 2024/01/31 批量插入 */
    @Override
    public int batchInsertRolePermission(List<RolePermission> list) {
        return mapper.insertBatchSomeColumn(list) ;
    }

    /** TODO: Written by - Han Yongding 2024/02/16 根据角色ID删除中中间表数据 */
    @Override
    public int deleteRolePermissionByRoleId(String roleId) {
        QueryWrapper<RolePermission> rolePermissionQueryWrapper = new QueryWrapper<>();
        rolePermissionQueryWrapper.eq("role_id", roleId) ;
        return mapper.delete(rolePermissionQueryWrapper) ;
    }
}
