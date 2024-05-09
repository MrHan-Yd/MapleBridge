package priv.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import priv.backend.domain.dto.Comment;
import priv.backend.domain.es.dto.ESComment;
import priv.backend.domain.vo.request.RestCountVO;
import priv.backend.mapper.CommentMapper;
import priv.backend.service.CommentService;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 评论表业务层实现类
 * @CreateDate :  2024-04-01 12:31
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService{

    /* TODO: Written by - Han Yongding 2024/04/01 注入评论表DAO */
    private final CommentMapper mapper ;

    @Autowired
    public CommentServiceImpl(CommentMapper commentMapper) {
        this.mapper = commentMapper ;
    }

    /* TODO: Written by - Han Yongding 2024/04/01 根据帖子Id查询所有评论 */
    @Override
    public List<ESComment> getAllCommentByPostId(String postId) {
        return mapper.getAllCommentByPostId(postId) ;
    }

    /* TODO: Written by - Han Yongding 2024/04/30 新增评论 */
    @Override
    public String addComment(Comment vo) {
        return mapper.insert(vo) == 1 ? vo.getId() : null ;
    }

    /* TODO: Written by - Han Yongding 2024/05/05 根据评论ID和版本号更新点赞数量 */
    @Override
    public Boolean updateCommentAndVersionById(RestCountVO vo) {
        return mapper.updateCommentAndVersionById(vo) == 1 ;
    }
    /* TODO: Written by - Han Yongding 2024/05/05 根据评论ID和版本号更新点赞数量 */
    @Override
    public Boolean updateUnCommentAndVersionById(RestCountVO vo) {
        return mapper.updateUnCommentAndVersionById(vo) == 1 ;
    }

    /* TODO: Written by - Han Yongding 2024/05/05 根据评论id查询点赞数量和版本号 */
    @Override
    public Comment getCommentLikeCountById(String id) {
        return mapper.getCommentLikeCountById(id) ;
    }

}
