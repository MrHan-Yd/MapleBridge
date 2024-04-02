package priv.backend.service;

import priv.backend.domain.es.dto.ESLike;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 点赞表业务层
 * @CreateDate :  2024-04-01 15:15
 */
public interface LikeService {

    /* TODO: Written by - Han Yongding 2024/04/01 获取帖子点赞情况 */
    List<ESLike> getPostLikeByPostId(String postId) ;

}
