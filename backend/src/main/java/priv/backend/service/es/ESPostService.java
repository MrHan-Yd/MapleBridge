package priv.backend.service.es;

import org.springframework.data.domain.Page;
import priv.backend.domain.PageBean;
import priv.backend.domain.es.dto.ESPost;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : ES Post业务层
 * @CreateDate :  2024-04-04 16:52
 */
public interface ESPostService {

    /* TODO: Written by - Han Yongding 2024/04/04 查询所有帖子数据 */
    Page<ESPost> getAllESPost(PageBean pageBean) ;
}
