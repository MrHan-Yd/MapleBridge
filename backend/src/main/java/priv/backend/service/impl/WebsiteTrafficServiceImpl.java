package priv.backend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import priv.backend.domain.PageBean;
import priv.backend.domain.dto.WebsiteTraffic;
import priv.backend.domain.vo.response.RespWebsiteTrafficVO;
import priv.backend.mapper.WebsiteTrafficMapper;
import priv.backend.service.WebsiteTrafficService;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 网站浏览记录表业务层接口实现类
 * @CreateDate :  2024-06-09 15:01
 */
@Service
public class WebsiteTrafficServiceImpl extends ServiceImpl<WebsiteTrafficMapper, WebsiteTraffic> implements WebsiteTrafficService {

    /* TODO: Written by - Han Yongding 2024/06/09 注入Mapper */
    @Resource
    private WebsiteTrafficMapper mapper;

    /* TODO: Written by - Han Yongding 2024/06/09 分页查询所有网站流量记录 */
    @Override
    public Page<RespWebsiteTrafficVO> queryAllWebsiteTraffic(PageBean pageBean) {
        Page<RespWebsiteTrafficVO> page = new Page<>(pageBean.getPageNum(), pageBean.getPageSize());
        return mapper.getAll(page);
    }
}
