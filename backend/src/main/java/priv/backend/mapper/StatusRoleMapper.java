package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import priv.backend.domain.dto.StatusRole;
import priv.backend.domain.vo.request.RestStatusRoleVO;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 角色状态DAO
 * @CreateDate :  2024-01-22 16:15
 */
public interface StatusRoleMapper extends BaseMapper<StatusRole> {

    /* TODO: Written by - Han Yongding 2024/01/23 分页查询所有角色状态 */
    Page<StatusRole> getAllStatusRoles(Page<StatusRole> page);
}
