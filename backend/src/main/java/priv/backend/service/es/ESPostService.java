package priv.backend.service.es;

import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.SearchHit;
import priv.backend.domain.PageBean;
import priv.backend.domain.PostPage;
import priv.backend.domain.es.dto.ESPost;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : ES Post业务层
 * @CreateDate :  2024-04-04 16:52
 */
public interface ESPostService {

    /* TODO: Written by - Han Yongding 2024/04/04 查询所有帖子数据 */
    Page<ESPost> getAllESPost(PostPage page) ;

    /* TODO: Written by - Han Yongding 2024/07/10 搜索 */
    List<String> searchSuggest(String queryString) ;

    /* TODO: Written by - Han Yongding 2024/07/12 分页搜索 */
    Page<SearchHit<ESPost>> search(String queryString, PageBean pageBean);
}
