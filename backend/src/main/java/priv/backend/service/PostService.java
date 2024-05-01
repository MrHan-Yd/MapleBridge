package priv.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import priv.backend.domain.PageBean;
import priv.backend.domain.dto.Post;
import priv.backend.domain.es.dto.ESPost;
import priv.backend.domain.vo.request.RestCommentVO;
import priv.backend.domain.vo.request.RestCountVO;
import priv.backend.domain.vo.request.RestPostVO;
import priv.backend.domain.vo.request.RestPostsVO;
import priv.backend.domain.vo.response.RespPostVO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 帖子业务层
 * @CreateDate :  2024-03-08 15:18
 */
public interface PostService {

    /** TODO: Written by - Han Yongding 2024/03/09 分页查询所有帖子 */
    Page<RespPostVO> getPagePost(PageBean pageBean);

    /** TODO: Written by - Han Yongding 2024/03/09 新增帖子 */
    String insertPost(RestPostVO vo);

    /* TODO: Written by - Han Yongding 2024/04/03 前端发布帖子，可能会上传图片资源 */
    String insertPost(RestPostsVO vo) ;

    /** TODO: Written by - Han Yongding 2024/03/10 修改帖子 */
    String updatePost(RestPostVO vo);

    /** TODO: Written by - Han Yongding 2024/03/10 删除帖子 */
    String deletePost(String postId);

    /* TODO: Written by - Han Yongding 2024/04/01 查询所有帖子，同步ES使用 */
    List<ESPost> getAllPostSyncES() ;

    /* TODO: Written by - Han Yongding 2024/04/03 根据postId查询帖子数据，同步ES使用 */
    ESPost getPostByPostIdSyncES(String postId) ;

    /* TODO: Written by - Han Yongding 2024/04/15 根据ID查询帖子版本号和点赞数量 */
    Post getLikeAndVersionByPostId(String postId) ;

    /* TODO: Written by - Han Yongding 2024/04/15 根据版本号和PostId更新帖子点赞数量 */
    Boolean updateLikeAndVersionByPostId(RestCountVO count) ;
    
    /* TODO: Written by - Han Yongding 2024/04/15 点赞帖子 */
    String likePost(RestCountVO vo)  ;

    /* TODO: Written by - Han Yongding 2024/04/15 插入点赞记录 */
    String insertLike(RestCountVO vo) ;

    /* TODO: Written by - Han Yongding 2024/04/15 删除点赞记录 */
    String deleteLikeByPostIdAndUserId(String postId, String userId) ;

    /* TODO: Written by - Han Yongding 2024/04/15 根据版本号和PostId更新帖子点赞数量 */
    Boolean updateUnLikeAndVersionByPostId(RestCountVO count) ;

    /* TODO: Written by - Han Yongding 2024/04/30 发布评论 */
    String commentPost(RestCommentVO vo) ;


    /* TODO: Written by - Han Yongding 2024/04/30 根据帖子ID查询评论数量和版本号 */
    Post getCommentAndVersionByPostId(String postId) ;

    /* TODO: Written by - Han Yongding 2024/04/30 根据版本号和PostId更新帖子评论数量 */
    Boolean updateCommentAndVersionByPostId(Post post);
}
