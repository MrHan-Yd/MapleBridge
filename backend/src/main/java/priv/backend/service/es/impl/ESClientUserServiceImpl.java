package priv.backend.service.es.impl;

import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import priv.backend.domain.PageBean;
import priv.backend.domain.es.dto.ESClientUser;
import priv.backend.repository.elasticsearch.ESClientUserRepository;
import priv.backend.service.es.ESClientUserService;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 客户端用户业务层接口实现类
 * @CreateDate :  2024-07-12 17:53
 */
@Service
public class ESClientUserServiceImpl implements ESClientUserService {
    /* TODO: Written by - Han Yongding 2024/07/12 注入ES用户数据层 */
    @Resource
    private ESClientUserRepository repository ;
    @Override
    public Page<SearchHit<ESClientUser>> searchByNickname(String nickname, PageBean pageBean) {
        if (ObjectUtils.isEmpty(nickname)) {
            return null;
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(pageBean.getPageNum() - 1, pageBean.getPageSize(), sort);
        return repository.findByNickname(nickname, pageable);
    }
}
