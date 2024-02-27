package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import priv.backend.domain.dto.User;
import priv.backend.domain.vo.response.RespUserVO;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 用户表DAO
 * @CreateDate :  2024-02-04 20:44
 */

public interface UserMapper extends BaseMapper<User> {

    /* TODO: Written by - Han Yongding 2024/02/12 分页查询所有用户信息 */
    Page<RespUserVO> getAllUser(@Param("page") Page<User> page) ;
}