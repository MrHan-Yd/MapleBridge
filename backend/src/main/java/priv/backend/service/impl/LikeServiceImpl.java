package priv.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
public class LikeServiceImpl implements LikeService {

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
}
