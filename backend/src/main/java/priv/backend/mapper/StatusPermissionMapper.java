package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Select;
import priv.backend.domain.dto.StatusPermission;
import priv.backend.domain.dto.StatusRole;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 权限状态DAO
 * @CreateDate :  2024-01-26 16:07
 */
public interface StatusPermissionMapper extends BaseMapper<StatusPermission> {
    /* TODO: Written by - Han Yongding 2024/01/26 分页查询所有权限状态 */
    @Select("select status_id, status_name, state, create_id, create_time, update_id, update_time from status_permission where state != '2'")
    Page<StatusPermission> getAllStatusPermission(Page<StatusPermission> page);
}
