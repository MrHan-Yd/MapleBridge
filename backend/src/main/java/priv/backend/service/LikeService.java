package priv.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import priv.backend.domain.dto.Like;
import priv.backend.domain.es.dto.ESLike;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 点赞表业务层
 * @CreateDate :  2024-04-01 15:15
 */
public interface LikeService extends IService<Like> {

    /* TODO: Written by - Han Yongding 2024/04/01 获取帖子点赞情况 */
    List<ESLike> getPostLikeByPostId(String postId) ;

    /* TODO: Written by - Han Yongding 2024/05/05 根据评论唯一标识符和帖子唯一标识符查询评论点赞情况 */
    List<ESLike> getPostLikeByPostIdAndCommentId(String postId, String commentId) ;

    /* TODO: Written by - Han Yongding 2024/04/15 插入帖子点赞记录 */
    boolean insertPostLike(Like like) ;

    /* TODO: Written by - Han Yongding 2024/04/15 根据PostId和userId查询点赞记录 */
    Boolean deleteLikeByPostIdAndUserId(String postId, String userId) ;

    /* TODO: Written by - Han Yongding 2024/04/15 根据CommentId和userId查删除点赞记录 */
    Boolean deleteLikeByCommentIdAndUserId(String commentId, String userId) ;

}
