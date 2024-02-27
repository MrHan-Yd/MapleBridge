package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import priv.backend.domain.dto.Permission;
import priv.backend.domain.vo.response.RespPermissionSelectVO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 权限表DAO
 * @CreateDate :  2024-01-27 12:55
 */

public interface PermissionMapper extends BaseMapper<Permission> {

    /* TODO: Written by - Han Yongding 2024/01/30 分页查询权限 */
    Page<Permission> getPagePermission(Page<Permission> page) ;

    /* TODO: Written by - Han Yongding 2024/01/30 查询所有权限，用于新增角色 */
    List<RespPermissionSelectVO> getAllPermission() ;
}
