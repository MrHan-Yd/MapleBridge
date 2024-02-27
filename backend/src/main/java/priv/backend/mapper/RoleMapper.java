package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import priv.backend.domain.dto.Role;
import priv.backend.domain.vo.response.RespRoleVO;
import priv.backend.domain.vo.response.RespUserRoleNoIdVO;
import priv.backend.domain.vo.response.RespUserRoleVO;
import java.util.List ;

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

    /* TODO: Written by - Han Yongding 2024/02/15 根据角色ID查询对应角色 */
    RespUserRoleVO getRoleById(String roleId) ;

    /* TODO: Written by - Han Yongding 2024/02/15 获取所有角色，不分页，用于前端列表展示选择 */
    List<RespUserRoleVO> getRoleSelect() ;
}
