package priv.backend.service;

import priv.backend.domain.dto.Comment;
import priv.backend.domain.es.dto.ESComment;
import priv.backend.domain.vo.request.RestCommentVO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 评论表业务层
 * @CreateDate :  2024-04-01 12:31
 */
public interface CommentService {
    /* TODO: Written by - Han Yongding 2024/04/01 根据帖子Id查询所有评论 */
    List<ESComment> getAllCommentByPostId(String postId) ;

    /* TODO: Written by - Han Yongding 2024/04/30 新增评论 */

    String addComment(Comment vo) ;
}
