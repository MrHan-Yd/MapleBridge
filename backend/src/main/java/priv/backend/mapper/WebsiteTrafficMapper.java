package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import priv.backend.domain.dto.WebsiteTraffic;
import priv.backend.domain.vo.response.RespWebsiteTrafficVO;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 网站浏览表Mapper
 * @CreateDate :  2024-06-09 14:56
 */
public interface WebsiteTrafficMapper extends BaseMapper<WebsiteTraffic> {

    /**
     * 分页查询网站流量记录
     * @param page 分页信息
     * @return 分页查询结果
     */
    Page<RespWebsiteTrafficVO> getAll(Page<RespWebsiteTrafficVO> page) ;
}
