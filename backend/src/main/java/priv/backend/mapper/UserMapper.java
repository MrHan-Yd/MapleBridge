package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import priv.backend.domain.dto.User;
import priv.backend.domain.es.dto.ESClientUser;
import priv.backend.domain.vo.response.RespUserVO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 用户表DAO
 * @CreateDate :  2024-02-04 20:44
 */

public interface UserMapper extends BaseMapper<User> {

    /** TODO: Written by - Han Yongding 2024/02/12 分页查询所有用户信息 */
    Page<RespUserVO> getAllUser(@Param("page") Page<RespUserVO> page) ;

    /* TODO: Written by - Han Yongding 2024/03/26 根据用户名获取邮箱 */
    String getEmailByAccount(String value) ;

    /* TODO: Written by - Han Yongding 2024/04/11 根据用户ID查询用户信息，同步ES使用 */
    ESClientUser getClientUserByUserId(String userId) ;

    /* TODO: Written by - Han Yongding 2024/04/07 查询所有用户信息，同步es使用 */
    List<ESClientUser> getClientUserES() ;
}
