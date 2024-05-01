package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import priv.backend.domain.dto.Post;
import priv.backend.domain.es.dto.ESPost;
import priv.backend.domain.vo.request.RestCountVO;
import priv.backend.domain.vo.response.RespPostVO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 帖子DAO
 * @CreateDate :  2024-03-08 15:12
 */
public interface PostMapper extends BaseMapper<Post> {

    /* TODO: Written by - Han Yongding 2024/03/09 分页查询所有帖子 */
    Page<RespPostVO> getPagePost(Page<RespPostVO> page) ;

    /* TODO: Written by - Han Yongding 2024/04/01 查询帖子数据，同步ES使用 */
    List<ESPost> getAllPostSyncES() ;

    /* TODO: Written by - Han Yongding 2024/04/03 根据postId查询帖子数据，同步ES使用 */
    ESPost getPostByPostIdSyncES(String postId) ;

    /* TODO: Written by - Han Yongding 2024/04/15 根据ID查询帖子版本号和点赞数量 */
    Post getLikeAndVersionByPostId(String postId) ;

    /* TODO: Written by - Han Yongding 2024/04/15 根据ID查询帖子版本号和评论数量 */
    Post getCommentAndVersionByPostId(String postId) ;

    /* TODO: Written by - Han Yongding 2024/04/15 根据版本号和PostId更新帖子点赞数量 */
    Integer updateLikeAndVersionByPostId(RestCountVO count) ;

    /* TODO: Written by - Han Yongding 2024/04/15 根据版本号和PostId更新帖子点赞记录 */
    Integer updateUnLikeAndVersionByPostId(RestCountVO count) ;

    /* TODO: Written by - Han Yongding 2024/04/30 根据版本号和PostId更新帖子评论数量 */
    Integer updateCommentAndVersionByPostId(Post post) ;
}
