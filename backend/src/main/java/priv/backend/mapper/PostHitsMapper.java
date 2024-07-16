package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import priv.backend.domain.dto.PostHits;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 帖子点击量DAO
 * @CreateDate :  2024-07-08 15:11
 */
public interface PostHitsMapper extends BaseMapper<PostHits> {

    /* TODO: Written by - Han Yongding 2024/07/08 根据帖子ID更新帖子点击量 */
    Integer updatePostHitsByPostId(PostHits postId);

    /* TODO: Written by - Han Yongding 2024/07/08 新增帖子点击量 */
    Integer insertPostHitsByPostId(PostHits postHits);

    /* TODO: Written by - Han Yongding 2024/07/09 根据帖子ID查询帖子点击量和版本 */
    PostHits selectPostHitsByPostId(String postId) ;

    /* TODO: Written by - Han Yongding 2024/07/09 根据帖子ID查询帖子点击量 */
    String getHitsByPostId(String postId) ;
}
