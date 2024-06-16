package priv.backend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import priv.backend.domain.PageBean;
import priv.backend.domain.dto.LoginLog;
import priv.backend.domain.vo.response.RespLoginLogVO;
import priv.backend.mapper.LoginLogMapper;
import priv.backend.service.LoginLogService;
import priv.backend.util.PageUtils;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 登录日志业务层实现类
 * @CreateDate :  2024-06-15 16:43
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

    /* TODO: Written by - Han Yongding 2024/06/16 注入登录日志数据层 */
    @Resource
    private LoginLogMapper mapper ;

    /* TODO: Written by - Han Yongding 2024/06/15 保存日志 */
    @Override
    public boolean saveLoginLog(LoginLog loginLog) {
        return this.save(loginLog);
    }

    /* TODO: Written by - Han Yongding 2024/06/16 分页查询所有登录日志 */
    @Override
    public Page<RespLoginLogVO> getAll(PageBean page) {
        Page<RespLoginLogVO> pageResult = new Page<>(page.getPageNum(), page.getPageSize());
        return mapper.getAll(pageResult);
    }
}
