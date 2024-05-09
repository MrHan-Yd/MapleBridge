package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import priv.backend.domain.dto.Comment;
import priv.backend.domain.es.dto.ESComment;
import priv.backend.domain.vo.request.RestCountVO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 评论表DAO
 * @CreateDate :  2024-04-01 12:27
 */
public interface CommentMapper extends BaseMapper<Comment> {
    /* TODO: Written by - Han Yongding 2024/04/01 根据帖子ID查询所有评论 */
    List<ESComment> getAllCommentByPostId(String postId) ;

    /* TODO: Written by - Han Yongding 2024/05/01 根据帖子ID和评论ID查询其子评论 */
    List<ESComment>getAllCommentByCommentId(String commentId) ;

    /* TODO: Written by - Han Yongding 2024/05/05 根据评论ID和版本号更新点赞数量 */
    Integer updateCommentAndVersionById(RestCountVO vo) ;
    /* TODO: Written by - Han Yongding 2024/05/05 根据评论ID和版本号更新点赞数量 */
    Integer updateUnCommentAndVersionById(RestCountVO vo) ;

    /* TODO: Written by - Han Yongding 2024/05/05 根据评论id查询点赞数量和版本号 */

    Comment getCommentLikeCountById(String CommentId) ;
}
