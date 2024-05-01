package priv.backend.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import priv.backend.domain.dto.FilePosts;
import priv.backend.domain.es.dto.ESComment;
import priv.backend.domain.es.dto.ESFilePost;
import priv.backend.domain.es.dto.ESPost;
import priv.backend.service.impl.CommentServiceImpl;
import priv.backend.service.impl.FilePostServiceImpl;
import priv.backend.service.impl.LikeServiceImpl;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : ES常用工具类
 * @CreateDate :  2024-04-04 12:05
 */
@Component
public class ElasticsearchUtils {

    /* TODO: Written by - Han Yongding 2024/04/01 注入评论表业务层 */
    private final CommentServiceImpl commentService ;

    /* TODO: Written by - Han Yongding 2024/04/01 注入点赞表业务层 */
    private final LikeServiceImpl likeService ;

    /* TODO: Written by - Han Yongding 2024/04/03 注入帖子文件业务层 */
    private final FilePostServiceImpl filePostService ;

    /* TODO: Written by - Han Yongding 2024/04/04 上载常用工具类 */
    private final UploadUtils uploadUtils ;

    @Autowired
    public ElasticsearchUtils(CommentServiceImpl commentService,
                              LikeServiceImpl likeService,
                              FilePostServiceImpl filePostService,
                              UploadUtils uploadUtils) {
        this.commentService = commentService ;
        this.likeService = likeService ;
        this.filePostService = filePostService ;
        this.uploadUtils = uploadUtils ;
    }

    /* TODO: Written by - Han Yongding 2024/04/04 帖子数据处理，补充 */
    public  ESPost ESPostDataHandle(ESPost post) {
        /* TODO: Written by - Han Yongding 2024/04/03 帖子评论数据 */
        List<ESComment> allCommentByPostId = commentService.getAllCommentByPostId(post.getPostId())
                .stream()
                .peek(c -> {
                    c.getUser().setPath(
                            uploadUtils.generateAccessPath(
                                    c.getUser()
                                            .getUserId(),
                                    c.getUser()
                                            .getPath()));
                    c.setSubComments(
                            c.getSubComments()
                                    .stream()
                                    .peek(d -> {
                                        d.getUser()
                                                .setPath(
                                                        uploadUtils
                                                                .generateAccessPath(
                                                                        d.getUser()
                                                                                .getUserId(),
                                                                        d.getUser()
                                                                                .getPath()));
                                    }).toList());
                }).toList();
        post.setComment(allCommentByPostId);
        /* TODO: Written by - Han Yongding 2024/04/30 帖子评论用户信息 */
        /* TODO: Written by - Han Yongding 2024/04/03 帖子点赞数据 */
        post.setLike(likeService.getPostLikeByPostId(post.getPostId()));
        /* TODO: Written by - Han Yongding 2024/04/03 帖子文件数据 */
        List<FilePosts> filePosts = filePostService.getFilePostByPostId(post.getPostId());
        List<ESFilePost> esFilePosts = filePosts.stream()
                .map(l -> {
                    ESFilePost viewObject = l.asViewObject(ESFilePost.class);
                    viewObject.setUrl(uploadUtils.generateAccessPath(l.getUserId(), "post/" + post.getPostId()) + l.getFileName() + l.getFileSuffix());
                    return viewObject;
                })
                .toList();
        post.setFilePost(esFilePosts);
        /* TODO: Written by - Han Yongding 2024/04/27 帖子发布用户信息 */
        post.getUser().setPath(uploadUtils.generateAccessPath(post.getUser().getUserId(), post.getUser().getPath()));
        /* TODO: Written by - Han Yongding 2024/04/04 返回处理好的数据 */
        return post ;
    }
}
