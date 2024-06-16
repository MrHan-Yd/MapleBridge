package priv.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import priv.backend.domain.PageBean;
import priv.backend.domain.dto.LoginLog;
import priv.backend.domain.vo.response.RespLoginLogVO;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 登录日志业务层
 * @CreateDate :  2024-06-15 16:42
 */
public interface LoginLogService extends IService <LoginLog> {
    /* TODO: Written by - Han Yongding 2024/06/15 保存日志 */
    boolean saveLoginLog(LoginLog loginLog) ;

    /* TODO: Written by - Han Yongding 2024/06/16 分页查询所有登录日志 */
    Page<RespLoginLogVO> getAll(PageBean page) ;
}
