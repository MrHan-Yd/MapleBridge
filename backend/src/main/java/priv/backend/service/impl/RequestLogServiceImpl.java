package priv.backend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import priv.backend.domain.PageBean;
import priv.backend.domain.dto.RequestLog;
import priv.backend.domain.vo.response.RespRequestLogVO;
import priv.backend.mapper.RequestLogMapper;
import priv.backend.service.RequestLogService;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 请求日志业务层实现类
 * @CreateDate :  2024-06-16 16:17
 */
@Service
public class RequestLogServiceImpl extends ServiceImpl<RequestLogMapper, RequestLog> implements RequestLogService {

    /* TODO: Written by - Han Yongding 2024/06/16 注入请求日志Mapper */
    @Resource
    private RequestLogMapper mapper;

    /* TODO: Written by - Han Yongding 2024/06/16 分页查询所有请求日志 */
    @Override
    public Page<RespRequestLogVO> getAll(PageBean page) {
        Page<RespRequestLogVO> pageResult = new Page<>(page.getPageNum(), page.getPageSize());
        return this.mapper.getAll(pageResult) ;
    }

    /* TODO: Written by - Han Yongding 2024/06/16 保存登录日志 */
    @Override
    public boolean saveRequestLog(RequestLog requestLog) {
        return this.save(requestLog) ;
    }
}
