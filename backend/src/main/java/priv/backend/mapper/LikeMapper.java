package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import priv.backend.domain.dto.Comment;
import priv.backend.domain.dto.Like;
import priv.backend.domain.es.dto.ESLike;

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

    /* TODO: Written by - Han Yongding 2024/05/05 根据评论唯一标识符和帖子唯一标识符查询评论点赞情况 */
    List<ESLike> getPostLikeByPostIdAndCommentId(String postId, String commentId) ;

    /* TODO: Written by - Han Yongding 2024/04/15 删除点赞记录 */
    Integer deleteLikeByPostIdAndUserId(String postId, String userId) ;

    /* TODO: Written by - Han Yongding 2024/04/15 根据CommentId和userId删除点赞记录*/
    Integer deleteLikeByCommentIdAndUserId(String commentId, String userId) ;
}
