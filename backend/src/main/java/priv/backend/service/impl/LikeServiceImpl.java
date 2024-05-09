package priv.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import priv.backend.domain.dto.Like;
import priv.backend.domain.es.dto.ESLike;
import priv.backend.mapper.LikeMapper;
import priv.backend.service.LikeService;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 点赞表业务层
 * @CreateDate :  2024-04-01 15:16
 */
@Service
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> implements LikeService {

    /* TODO: Written by - Han Yongding 2024/04/01 注入点赞表DAO */
    private final LikeMapper likeMapper ;

    @Autowired
    public LikeServiceImpl(LikeMapper likeMapper) {
        this.likeMapper = likeMapper ;
    }

    /* TODO: Written by - Han Yongding 2024/04/01 获取帖子点赞情况 */
    @Override
    public List<ESLike> getPostLikeByPostId(String postId) {
        return likeMapper.getPostLikeByPostId(postId) ;
    }

    /* TODO: Written by - Han Yongding 2024/05/05 根据评论唯一标识符和帖子唯一标识符查询评论点赞情况 */
    @Override
    public List<ESLike> getPostLikeByPostIdAndCommentId(String postId, String commentId) {
        return likeMapper.getPostLikeByPostIdAndCommentId(postId, commentId) ;
    }

    /* TODO: Written by - Han Yongding 2024/04/15 插入帖子点赞记录 */
    @Override
    public boolean insertPostLike(Like like)  {
        return this.save(like) ;
    }

    @Override
        /* TODO: Written by - Han Yongding 2024/04/15 根据PostId和userId查询点赞记录 */
    public Boolean deleteLikeByPostIdAndUserId(String postId, String userId) {
        return likeMapper.deleteLikeByPostIdAndUserId(postId, userId) >= 1;
    }

    /* TODO: Written by - Han Yongding 2024/04/15 根据CommentId和userId删除点赞记录 */
    @Override
    public Boolean deleteLikeByCommentIdAndUserId(String commentId, String userId) {
        return likeMapper.deleteLikeByCommentIdAndUserId(commentId, userId) >= 1;
    }
}
