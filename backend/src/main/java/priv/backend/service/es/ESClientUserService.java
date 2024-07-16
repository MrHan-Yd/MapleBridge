package priv.backend.service.es;

import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.SearchHit;
import priv.backend.domain.PageBean;
import priv.backend.domain.es.dto.ESClientUser;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 客户端用户业务层接口
 * @CreateDate :  2024-07-12 17:52
 */
public interface ESClientUserService {

    /* TODO: Written by - Han Yongding 2024/07/12 根据用户名查询对应的用户信息 */
    Page<SearchHit<ESClientUser>> searchByNickname(String nickname, PageBean pageBean) ;
}
