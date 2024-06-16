package priv.backend.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import priv.backend.domain.es.dto.ESClientUser;
import priv.backend.repository.elasticsearch.ESClientUserRepository;
import priv.backend.service.impl.PostServiceImpl;
import priv.backend.service.impl.UserServiceImpl;
import priv.backend.util.LogUtils;
import priv.backend.util.TimeUtils;
import priv.backend.util.UploadUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 客户端用户保存信息后同步至ES队列监听器
 * @CreateDate :  2024-04-11 18:49
 */
@Slf4j
@Component
@RabbitListener(queues = "clientUserSyncES")
public class ClientUserSyncESQueueListener {
    /* TODO: Written by - Han Yongding 2024/04/11 注入用户业务层实现类 */
    private final UserServiceImpl userService ;

    /* TODO: Written by - Han Yongding 2024/04/11 注入用户ES DAO */
    private final ESClientUserRepository repository ;

    /* TODO: Written by - Han Yongding 2024/04/12 注入上载工具类 */
    private final UploadUtils uploadUtils ;

    /* TODO: Written by - Han Yongding 2024/06/16 注入post业务层实现类 */
    private final PostServiceImpl postService ;

    /* TODO: Written by - Han Yongding 2024/06/16 注入RabbitMQ模板 */
    private final AmqpTemplate template ;

    @Autowired
    public ClientUserSyncESQueueListener(UserServiceImpl userService,
                                         ESClientUserRepository repository,
                                         UploadUtils uploadUtils,
                                         PostServiceImpl postService,
                                         AmqpTemplate template) {
        this.userService = userService ;
        this.repository = repository ;
        this.uploadUtils = uploadUtils ;
        this.postService = postService ;
        this.template = template ;
    }

    @RabbitHandler
    public void postSyncElasticsearch(String userId) {
        LogUtils.info(this.getClass(),"客户端修改信息，队列执行：user表数据同步Elasticsearch任务开始");

        // 记录开始时间
        TimeUtils.start();

        /* TODO: Written by - Han Yongding 2024/04/11 从数据库中获取数据 */
        LogUtils.info(this.getClass(),"正在获取数据");
        ESClientUser dto = userService.getClientUserByUserId(userId);
        /* TODO: Written by - Han Yongding 2024/04/12 访问路径生成 */
        if (dto.getAvatars() != null) {
            dto.getAvatars().setAvatarPath(uploadUtils.generateAccessPath(dto.getId(), "avatars"));

        }
        LogUtils.info(this.getClass(),"数据已成功获取");

        LogUtils.info(this.getClass(),"正在同步");
        ESClientUser save = repository.save(dto);

        /* TODO: Written by - Han Yongding 2024/04/11 同步失败 */
        if (save.getId() == null) {
            log.info("数据同步失败，原因：同步ES失败");
        }

        /* TODO: Written by - Han Yongding 2024/06/16 更新ES中用户相关数据 */
        postService.getPostIdByUserId(dto.getId()).forEach(id -> {
            template.convertAndSend("postSyncES", id) ;
        });

        /* TODO: Written by - Han Yongding 2024/04/11 同步成功 */
        LogUtils.info(this.getClass(),"客户端修改信息，队列执行：user表数据已同步至Elasticsearch");
        /* TODO: Written by - Han Yongding 2024/04/11 计算同步耗时 */
        LogUtils.info(this.getClass(),"同步耗时" + TimeUtils.getCurrentTime(TimeUtils.getStartTime()));
        /* TODO: Written by - Han Yongding 2024/04/11 任务结束 */
        LogUtils.info(this.getClass(),"客户端修改信息，队列执行完毕：user表同步Elasticsearch任务结束");

    }
}
