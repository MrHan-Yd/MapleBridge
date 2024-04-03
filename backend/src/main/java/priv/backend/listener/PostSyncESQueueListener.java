package priv.backend.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import priv.backend.domain.es.dto.ESPost;
import priv.backend.repository.ESPostRepository;
import priv.backend.service.impl.CommentServiceImpl;
import priv.backend.service.impl.FilePostServiceImpl;
import priv.backend.service.impl.LikeServiceImpl;
import priv.backend.service.impl.PostServiceImpl;
import priv.backend.util.LogUtils;
import priv.backend.util.TimeUtils;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 作用
 * @CreateDate :  2024-04-03 22:52
 */
@Slf4j
@Component
@RabbitListener(queues = "postSyncES")
public class PostSyncESQueueListener {
    /* TODO: Written by - Han Yongding 2024/04/03 注入Post业务层 */
    private final PostServiceImpl postService ;

    /* TODO: Written by - Han Yongding 2024/04/03 注入评论表业务层 */
    private final CommentServiceImpl commentService ;

    /* TODO: Written by - Han Yongding 2024/04/03 注入点赞表业务层 */
    private final LikeServiceImpl likeService ;

    /* TODO: Written by - Han Yongding 2024/04/03 注入post文件数据表业务层 */
    private final FilePostServiceImpl filePostService ;

    /* TODO: Written by - Han Yongding 2024/04/03 注入帖子ES DAO */
    private final ESPostRepository postRepository;

    @Autowired
    public PostSyncESQueueListener(PostServiceImpl postService,
                                   CommentServiceImpl commentService,
                                   LikeServiceImpl likeService,
                                   FilePostServiceImpl filePostService,
                                   ESPostRepository postRepository) {
        this.postService = postService ;
        this.commentService = commentService ;
        this.likeService = likeService ;
        this.filePostService = filePostService ;
        this.postRepository = postRepository ;
    }

    @RabbitHandler
    public void postSyncElasticsearch(String postId) {
        LogUtils.info(this.getClass(),"发布帖子，队列执行：post表同步Elasticsearch任务开始");
        // 记录开始时间
        TimeUtils.start();
        /* TODO: Written by - Han Yongding 2024/04/03 从数据库中获取数据 */
        LogUtils.info(this.getClass(),"正在获取数据");
        ESPost esPost = postService.getPostByPostIdSyncES(postId) ;

        LogUtils.info(this.getClass(),"数据已成功获取");

        /* TODO: Written by - Han Yongding 2024/04/03 数据处理，补充帖子的评论和点赞关联的数据 */
        LogUtils.info(this.getClass(),"正在处理数据");

        /* TODO: Written by - Han Yongding 2024/04/03 评论数据 */
        esPost.setComment(commentService.getAllCommentByPostId(postId)) ;
        LogUtils.info(this.getClass(),"评论数据整理完成");

        /* TODO: Written by - Han Yongding 2024/04/03 点赞数据 */
        esPost.setLike(likeService.getPostLikeByPostId(postId)) ;
        LogUtils.info(this.getClass(),"点赞数据整理完成");

        /* TODO: Written by - Han Yongding 2024/04/03 帖子文件数据 */
        esPost.setFilePost(filePostService.getFilePostByPostId(postId));
        LogUtils.info(this.getClass(),"帖子文件数据整理完成");
        LogUtils.info(this.getClass(),"数据处理完成，正在同步");

        /* TODO: Written by - Han Yongding 2024/04/03 同步ES */
        ESPost save = postRepository.save(esPost);

        /* TODO: Written by - Han Yongding 2024/04/03 同步失败 */
        if (save.getPostId() == null) {
            log.info("数据同步失败，原因：存入ES失败");
        }

        /* TODO: Written by - Han Yongding 2024/04/03 同步成功 */
        LogUtils.info(this.getClass(),"发布帖子，队列执行：post表数据已同步至Elasticsearch");
        /* TODO: Written by - Han Yongding 2024/04/03 计算初始化耗时 */
        LogUtils.info(this.getClass(),"同步耗时" + TimeUtils.getCurrentTime(TimeUtils.getStartTime()));
        LogUtils.info(this.getClass(),"发布帖子，队列执行完毕：post表同步Elasticsearch任务结束");
    }
}
