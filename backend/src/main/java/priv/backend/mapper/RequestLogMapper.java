package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import priv.backend.domain.dto.RequestLog;
import priv.backend.domain.vo.response.RespRequestLogVO;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 请求日志Mapper接口
 * @CreateDate :  2024-06-16 16:15
 */
public interface RequestLogMapper extends BaseMapper<RequestLog> {

    /* TODO: Written by - Han Yongding 2024/06/16 分页查询所有请求日志 */
    Page<RespRequestLogVO> getAll(Page<RespRequestLogVO> page) ;
}
