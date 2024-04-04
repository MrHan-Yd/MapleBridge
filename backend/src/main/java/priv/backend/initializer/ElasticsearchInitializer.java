package priv.backend.initializer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import priv.backend.domain.es.dto.ESPost;
import priv.backend.repository.ESPostRepository;
import priv.backend.service.impl.PostServiceImpl;
import priv.backend.util.CurrentUtils;
import priv.backend.util.ElasticsearchUtils;
import priv.backend.util.LogUtils;
import priv.backend.util.TimeUtils;

import java.util.List;
import java.util.stream.StreamSupport;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 初始化ES
 * @CreateDate :  2024-04-01 9:38
 */
@Slf4j
@Component
public class ElasticsearchInitializer implements CommandLineRunner {

    /* TODO: Written by - Han Yongding 2024/04/01 注入帖子表业务层 */
    private final PostServiceImpl postService ;


    /* TODO: Written by - Han Yongding 2024/04/01 注入帖子ES DAO */
    private final ESPostRepository postRepository;

    /* TODO: Written by - Han Yongding 2024/04/04 注入ES常用工具类 */
    private final ElasticsearchUtils elasticsearchUtils ;


    @Autowired
    public ElasticsearchInitializer(ESPostRepository postRepository,
                                    PostServiceImpl postService,
                                    ElasticsearchUtils elasticsearchUtils) {
        this.postRepository = postRepository;
        this.postService = postService ;
        this.elasticsearchUtils = elasticsearchUtils ;
    }

    /* TODO: Written by - Han Yongding 2024/04/01 SpringBoot启动时此run方法会自动调用 */
    @Override
    public void run(String... args) {
        LogUtils.info(this.getClass(),"post表同步Elasticsearch任务开始");

        // 记录开始时间
        TimeUtils.start();

        LogUtils.info(this.getClass(),"正在获取数据");

        /* TODO: Written by - Han Yongding 2024/04/01 从数据库中获取所有post表数据 */
        List<ESPost> esPost = postService.getAllPostSyncES() ;

        LogUtils.info(this.getClass(),"数据已成功获取");

        LogUtils.info(this.getClass(),"正在处理数据");
        /* TODO: Written by - Han Yongding 2024/04/01 数据处理，补充帖子的评论和点赞关联的数据 */
        List<ESPost> postList = esPost.stream()
                .map(elasticsearchUtils::ESPostDataHandle).toList() ;
        LogUtils.info(this.getClass(),"数据处理完成，正在同步");

        /* TODO: Written by - Han Yongding 2024/04/01 同步到ES中 */
        Iterable<ESPost> esPosts = postRepository.saveAll(postList);

        /* TODO: Written by - Han Yongding 2024/04/01 长度不相等则初始化失败 */
        if (CurrentUtils.sizeWhetherUnequal(
                postList.size(),
                StreamSupport.stream(esPosts.spliterator(), false)
                .toList().size())) {
            log.info("数据同步失败，原因：数据丢失或者部分数据丢失");
            return ;
        }

        LogUtils.info(this.getClass(),"post表数据已同步至Elasticsearch");

        /* TODO: Written by - Han Yongding 2024/04/01 计算初始化耗时 */
        LogUtils.info(this.getClass(),"同步耗时" + TimeUtils.getCurrentTime(TimeUtils.getStartTime()));
        LogUtils.info(this.getClass(),"post表同步Elasticsearch任务结束");
    }
}
