package priv.backend.service.impl;

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
    @Override

    /* TODO: Written by - Han Yongding 2024/01/31 批量插入 */
    public int batchInsertRolePermission(List<RolePermission> list) {
        return mapper.insertBatchSomeColumn(list) ;
    }
}
