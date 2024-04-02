package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import priv.backend.domain.dto.Comment;
import priv.backend.domain.es.dto.ESComment;

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
}
