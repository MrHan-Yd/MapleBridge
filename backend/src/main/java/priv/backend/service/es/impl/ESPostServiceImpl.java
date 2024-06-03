package priv.backend.service.es.impl;

import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import priv.backend.domain.PageBean;
import priv.backend.domain.es.dto.ESPost;
import priv.backend.repository.elasticsearch.ESPostRepository;
import priv.backend.service.es.ESPostService;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : ES Post业务层实现类
 * @CreateDate :  2024-04-04 16:52
 */
@Service
public class ESPostServiceImpl implements ESPostService {

    /* TODO: Written by - Han Yongding 2024/04/04 注入ES Post DAO层 */
    @Resource
    private ESPostRepository esPostRepository ;


    /* TODO: Written by - Han Yongding 2024/04/04 查询所有帖子数据 */
    @Override
    public Page<ESPost> getAllESPost(PageBean pageBean)  {
        Pageable pageable = PageRequest.of(pageBean.getPageNum() - 1, pageBean.getPageSize());
        return esPostRepository.findAll(pageable) ;
    }

}
