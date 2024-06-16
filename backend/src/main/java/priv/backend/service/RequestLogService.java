package priv.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import priv.backend.domain.PageBean;
import priv.backend.domain.dto.RequestLog;
import priv.backend.domain.vo.response.RespRequestLogVO;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 请求日志业务层接口
 * @CreateDate :  2024-06-16 16:17
 */
public interface RequestLogService extends IService<RequestLog> {

    /* TODO: Written by - Han Yongding 2024/06/16 分页查询所有请求日志 */
    Page<RespRequestLogVO> getAll(PageBean page) ;

    /* TODO: Written by - Han Yongding 2024/06/16 保存登录日志 */
    boolean saveRequestLog(RequestLog requestLog) ;
}
