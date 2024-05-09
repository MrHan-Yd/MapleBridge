package priv.backend.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import priv.backend.domain.dto.Comment;
import priv.backend.domain.dto.Post;
import priv.backend.domain.vo.request.RestCountVO;
import priv.backend.service.impl.CommentServiceImpl;
import priv.backend.service.impl.PostServiceImpl;
import priv.backend.util.LogUtils;
import priv.backend.util.TimeUtils;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 评论取消点赞监听器
 * @CreateDate :  2024-05-05 9:50
 */
@Component
@Slf4j
@RabbitListener(queues = "commentUnLikeRetry")
public class CommentUnLikeListener {

    /* TODO: Written by - Han Yongding 2024/04/15 注入帖子业务层实现类 */
    private final PostServiceImpl postService;

    /* TODO: Written by - Han Yongding 2024/04/15 注入post队列，用于同步更新帖子数据*/
    private final AmqpTemplate amqpTemplate ;

    /* TODO: Written by - Han Yongding 2024/05/05 注入评论表业务层 */
    private final CommentServiceImpl commentService ;

    public CommentUnLikeListener(PostServiceImpl postService,
                                 AmqpTemplate amqpTemplate,
                                 CommentServiceImpl commentService) {
        this.postService = postService;
        this.amqpTemplate = amqpTemplate;
        this.commentService = commentService;
    }

    @RabbitHandler
    public void receiveUnCommentLikeRetry(RestCountVO vo) {
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

        LogUtils.info(this.getClass(), "正在准备删除点赞记录");

        /* TODO: Written by - Han Yongding 2024/04/15 插入点赞表 */
        if (postService.deleteLikeByCommentIdAndUserId(vo.getCommentId(), vo.getUserId()) != null) {
            LogUtils.info(this.getClass(), "删除点赞记录失败，放弃本次重试任务");
            retries = maxRetries ;
        }

        LogUtils.info(this.getClass(), "正在尝试更新减少点赞数量");
        /* TODO: Written by - Han Yongding 2024/04/15 尝试更新点赞数量和版本号 */
        while (!updated && retries < maxRetries) {
            LogUtils.info(this.getClass(), "第" + (retries + 1) + "次尝试更新点赞数量");

            /* TODO: Written by - Han Yongding 2024/04/15 删除成功 */

            /* TODO: Written by - Han Yongding 2024/04/15 更新点赞数量和版本号，如果成功则退出循环 */

            LogUtils.info(this.getClass(), "正在尝试更新点赞数量和版本号");
            updated = commentService.updateUnCommentAndVersionById(vo);

            /* TODO: Written by - Han Yongding 2024/04/15 成功则同步更新单条数据 */
            amqpTemplate.convertAndSend("postSyncES", vo.getId()) ;

            if (!updated) {
                LogUtils.info(this.getClass(), "点赞数量和版本号更新失败");

                LogUtils.info(this.getClass(), "正在获取最新版本号");
                /* TODO: Written by - Han Yongding 2024/04/15 如果更新失败，获取最新版本号并重试 */
                Comment comment = commentService.getCommentLikeCountById(vo.getCommentId());
                if (comment != null) {
                    LogUtils.info(this.getClass(), "最新版本号获取成功");
                    vo.setVersion(comment.getVersion());
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
