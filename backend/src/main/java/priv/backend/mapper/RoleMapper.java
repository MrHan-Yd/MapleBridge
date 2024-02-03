package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import priv.backend.domain.dto.Role;
import priv.backend.domain.vo.response.RespRoleVO;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 角色DAO层
 * @CreateDate :  2024-01-28 20:44
 */
public interface RoleMapper extends BaseMapper<Role> {
//    @Select("select role_id, role_name, role_name_cn, status_id, create_id, create_time, update_id, update_time from role where status_id != '1749415120449003521'")
//    Page<Role> getAllRole(Page<Role> page) ;

    /* TODO: Written by - Han Yongding 2024/01/31 分页查询角色 */
    Page<RespRoleVO> getRole(@Param("page") Page<Role> page);
}
