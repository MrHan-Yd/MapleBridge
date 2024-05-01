package priv.backend.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import priv.backend.domain.dto.Post;
import priv.backend.domain.vo.request.RestCountVO;
import priv.backend.service.impl.PostServiceImpl;
import priv.backend.util.LogUtils;
import priv.backend.util.TimeUtils;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 帖子点赞重试监听器
 * @CreateDate :  2024-04-15 13:47
 */
@Component
@Slf4j
@RabbitListener(queues = "postLikeRetry")
public class PostLikeListener {

    /* TODO: Written by - Han Yongding 2024/04/15 注入帖子业务层实现类 */
    private final PostServiceImpl postService;

    /* TODO: Written by - Han Yongding 2024/04/15 注入post队列，用于同步更新帖子数据*/
    private final AmqpTemplate amqpTemplate ;

    @Autowired
    public PostLikeListener(PostServiceImpl postService,
                            AmqpTemplate amqpTemplate) {
        this.postService = postService;
        this.amqpTemplate = amqpTemplate;
    }

    @RabbitHandler
    public void receivePostLikeRetry(RestCountVO vo) {
        /* TODO: Written by - Han Yongding 2024/04/15 记录开始时间 */
        TimeUtils.start();

        LogUtils.info(this.getClass(), "初始化基础属性");
        /* TODO: Written by - Han Yongding 2024/04/15 是否更新成功 */
        boolean updated = false;
        /* TODO: Written by - Han Yongding 2024/04/15 最大重试次数 */
        int maxRetries = 10;
        /* TODO: Written by - Han Yongding 2024/04/15 重试次数  */
        int retries = 0;
        LogUtils.info(this.getClass(), "基础属性初始化完成");

        LogUtils.info(this.getClass(), "正在准备插入点赞记录");

        /* TODO: Written by - Han Yongding 2024/04/15 插入点赞表 */
        if (postService.insertLike(vo) != null) {
            LogUtils.info(this.getClass(), "插入点赞记录失败，放弃本次重试任务");
            retries = maxRetries ;
        }

        LogUtils.info(this.getClass(), "正在尝试更新点赞数量");
        /* TODO: Written by - Han Yongding 2024/04/15 尝试更新点赞数量和版本号 */
        while (!updated && retries < maxRetries) {
            LogUtils.info(this.getClass(), "第" + String.valueOf(retries + 1) + "次尝试更新点赞数量");

            /* TODO: Written by - Han Yongding 2024/04/15 插入成功 */

            /* TODO: Written by - Han Yongding 2024/04/15 更新点赞数量和版本号，如果成功则退出循环 */

            LogUtils.info(this.getClass(), "正在尝试更新点赞数量和版本号");
            updated = postService.updateLikeAndVersionByPostId(vo);

            /* TODO: Written by - Han Yongding 2024/04/15 成功则同步更新单条数据 */
            amqpTemplate.convertAndSend("postSyncES", vo.getId()) ;

            if (!updated) {
                LogUtils.info(this.getClass(), "赞数量和版本号更新失败");

                LogUtils.info(this.getClass(), "正在获取最新版本号");
                /* TODO: Written by - Han Yongding 2024/04/15 如果更新失败，获取最新版本号并重试 */
                Post post = postService.getLikeAndVersionByPostId(vo.getId());
                if (post != null) {
                    LogUtils.info(this.getClass(), "最新版本号获取成功");
                    vo.setVersion(post.getVersion());
                }
                LogUtils.info(this.getClass(), "等待进行下次重试");
                /* TODO: Written by - Han Yongding 2024/04/15 重试次数加一 */
                retries++;
                try {
                    LogUtils.info(this.getClass(), "休眠0.2秒");
                    /* TODO: Written by - Han Yongding 2024/04/15 重试间隔，可以根据需要调整 */
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    /* TODO: Written by - Han Yongding 2024/04/15 记录日志并重新设置中断状态 */
                    LogUtils.warning(this.getClass(), "线程被中断");
                    /* TODO: Written by - Han Yongding 2024/04/15 中断线程 */
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
