package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import priv.backend.domain.dto.LoginLog;
import priv.backend.domain.vo.response.RespLoginLogVO;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 登录日志mapper
 * @CreateDate :  2024-06-15 16:42
 */
public interface LoginLogMapper extends BaseMapper<LoginLog> {

    /* TODO: Written by - Han Yongding 2024/06/16 分页查询所有登录日志 */
    Page<RespLoginLogVO> getAll(Page<RespLoginLogVO> page) ;
}
