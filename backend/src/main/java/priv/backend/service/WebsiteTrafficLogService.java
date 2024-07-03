package priv.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import priv.backend.domain.PageBean;
import priv.backend.domain.dto.WebsiteTrafficLog;
import priv.backend.domain.vo.response.RespWebsiteTrafficLogVO;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 网站流量表业务层接口
 * @CreateDate :  2024-06-09 15:00
 */
public interface WebsiteTrafficLogService extends IService<WebsiteTrafficLog> {

    /* TODO: Written by - Han Yongding 2024/06/09 查询所有网站流量记录 */
    Page<RespWebsiteTrafficLogVO> queryAllWebsiteTraffic(PageBean pageBean);
}
