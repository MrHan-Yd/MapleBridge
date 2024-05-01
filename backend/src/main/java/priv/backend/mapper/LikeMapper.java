package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import priv.backend.domain.dto.Like;
import priv.backend.domain.dto.Post;
import priv.backend.domain.es.dto.ESLike;
import priv.backend.domain.vo.request.RestCountVO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 点赞表DAO
 * @CreateDate :  2024-04-01 15:14
 */
public interface LikeMapper extends BaseMapper<Like> {
    /* TODO: Written by - Han Yongding 2024/04/01 根据帖子唯一标识获取帖子点赞情况 */
    List<ESLike> getPostLikeByPostId(String postId) ;

    /* TODO: Written by - Han Yongding 2024/04/01 根据评论唯一标识查询点赞情况 */
    List<ESLike> getESCommentLikeByCommentId(String commentId) ;

    /* TODO: Written by - Han Yongding 2024/04/15 删除点赞记录 */
    Integer deleteLikeByPostIdAndUserId(String postId, String userId) ;
}
