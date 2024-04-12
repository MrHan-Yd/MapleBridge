package priv.backend.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import priv.backend.domain.es.dto.ESPost;
import priv.backend.repository.ESPostRepository;
import priv.backend.service.impl.PostServiceImpl;
import priv.backend.util.ElasticsearchUtils;
import priv.backend.util.LogUtils;
import priv.backend.util.TimeUtils;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 客户端发布帖子，同步ES队列监听器
 * @CreateDate :  2024-04-03 22:52
 */
@Slf4j
@Component
@RabbitListener(queues = "postSyncES")
public class PostSyncESQueueListener {
    /* TODO: Written by - Han Yongding 2024/04/03 注入Post业务层 */
    private final PostServiceImpl postService ;


    /* TODO: Written by - Han Yongding 2024/04/03 注入帖子ES DAO */
    private final ESPostRepository postRepository;

    /* TODO: Written by - Han Yongding 2024/04/04 注入ES常用工具类 */
    private final ElasticsearchUtils elasticsearchUtils ;

    @Autowired
    public PostSyncESQueueListener(PostServiceImpl postService,
                                   ESPostRepository postRepository,
                                   ElasticsearchUtils elasticsearchUtils) {
        this.postService = postService ;
        this.postRepository = postRepository ;
        this.elasticsearchUtils = elasticsearchUtils ;
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

        /* TODO: Written by - Han Yongding 2024/04/04 数据整理 */
        esPost = elasticsearchUtils.ESPostDataHandle(esPost) ;

        LogUtils.info(this.getClass(),"数据处理完成，正在同步");

        /* TODO: Written by - Han Yongding 2024/04/03 同步ES */
        ESPost save = postRepository.save(esPost);

        /* TODO: Written by - Han Yongding 2024/04/03 同步失败 */
        if (save.getPostId() == null) {
            log.info("数据同步失败，原因：同步ES失败");
        }

        /* TODO: Written by - Han Yongding 2024/04/03 同步成功 */
        LogUtils.info(this.getClass(),"发布帖子，队列执行：post表数据已同步至Elasticsearch");
        /* TODO: Written by - Han Yongding 2024/04/03 计算同步耗时 */
        LogUtils.info(this.getClass(),"同步耗时" + TimeUtils.getCurrentTime(TimeUtils.getStartTime()));
        LogUtils.info(this.getClass(),"发布帖子，队列执行完毕：post表同步Elasticsearch任务结束");
    }
}
