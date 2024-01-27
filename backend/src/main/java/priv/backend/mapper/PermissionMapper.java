package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Select;
import priv.backend.domain.dto.Permission;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 权限表DAO
 * @CreateDate :  2024-01-27 12:55
 */

public interface PermissionMapper extends BaseMapper<Permission> {

    @Select("select permission_id, permission_name, permission.permission_url, status_id, create_id, create_time, update_id, update_time from permission where status_id != '1750807870989885442'")
    Page<Permission> getAllPermission(Page<Permission> page) ;
}
