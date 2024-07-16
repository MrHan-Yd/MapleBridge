package priv.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import priv.backend.domain.dto.PostHits;
import priv.backend.domain.vo.request.RestPostHits;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 帖子点击量业务层
 * @CreateDate :  2024-07-08 15:12
 */
public interface PostHitsService extends IService<PostHits> {

    /* TODO: Written by - Han Yongding 2024/07/08 帖子点击量记录 */
    String postHitsByPostId(List<RestPostHits> postIds) ;

    /* TODO: Written by - Han Yongding 2024/07/09 根据帖子ID查询帖子点击量 */
    String getHitsByPostId(String postId) ;
}
