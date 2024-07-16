package priv.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;
import priv.backend.domain.dto.PostHits;
import priv.backend.domain.vo.request.RestPostHits;
import priv.backend.enumeration.RespMessageEnum;
import priv.backend.mapper.PostHitsMapper;
import priv.backend.service.PostHitsService;
import priv.backend.util.CurrentUtils;
import priv.backend.util.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 帖子点击量业务层实现类
 * @CreateDate :  2024-07-08 15:12
 */
@Service
public class PostHitsServiceImpl extends ServiceImpl<PostHitsMapper, PostHits> implements PostHitsService {

    @Resource
    private PostHitsMapper mapper;

    @Resource
    private AmqpTemplate amqpTemplate;

    @Override
    public String postHitsByPostId(List<RestPostHits> postIds) {
        /* TODO: Written by - Han Yongding 2024/07/08 为空 */
        if (postIds.isEmpty()) {
            return RespMessageEnum.PARAM_NOT_NULL.getMessage();
        }

        /* TODO: Written by - Han Yongding 2024/07/09 新增集合 */
        List<PostHits> insertList = postIds.stream()
                .filter(f -> !this.existPostHitsByPostId(f.getPostId()))
                .map(rp -> new PostHits(rp.getPostId(), rp.getHits())).toList();


        /* TODO: Written by - Han Yongding 2024/07/09 修改集合 */
        List<PostHits> updateList = postIds.stream()
                .filter(f -> this.existPostHitsByPostId(f.getPostId()))
                .map(rp -> new PostHits(rp.getPostId(), rp.getHits())).toList();

        /* TODO: Written by - Han Yongding 2024/07/08 处理结果 */
        boolean resultUpdate = false, resultInsert = false;

        /* TODO: Written by - Han Yongding 2024/07/08 不存在--新增 */
        if (!insertList.isEmpty()) {
            resultInsert = this.asyncInsertPostHits(insertList);
        }

        /* TODO: Written by - Han Yongding 2024/07/08 存在--更新 */
        if (!updateList.isEmpty()) {
            resultUpdate = this.asyncUpdatePostHits(updateList) ;
        }

        /* TODO: Written by - Han Yongding 2024/07/08 一条都没有处理成功，请求失败 */
        if (resultUpdate && resultInsert) {
            return RespMessageEnum.FAILED.getMessage();
        }

        /* TODO: Written by - Han Yongding 2024/07/08 业务结束，请求成功 */
        return null;
    }

    /* TODO: Written by - Han Yongding 2024/07/08 异步新增 */
    private boolean asyncInsertPostHits(List<PostHits> postHits) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        /* TODO: Written by - Han Yongding 2024/07/08 新增成功的帖子唯一标识 */
        ConcurrentSkipListSet<String> successSet = new ConcurrentSkipListSet<>();

        postHits.forEach(vo -> {
            /* TODO: Written by - Han Yongding 2024/07/08 异步新增 */
            CompletableFuture<Void> booleanCompletableFuture =
                    CompletableFuture.runAsync(() -> {
                        boolean result = !CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.insertPostHitsByPostId(vo));
                        /* TODO: Written by - Han Yongding 2024/07/08 新增成功 */
                        if (result) {
                            successSet.add(vo.getPostId());
                        }
                    });
            futures.add(booleanCompletableFuture);
        });

        /* TODO: Written by - Han Yongding 2024/07/08 等待全部任务完成 */
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        /* TODO: Written by - Han Yongding 2024/07/08 将需要更新的postId扔入队列，同步到ES */
        if (!successSet.isEmpty()) {
            this.synchronizationPostByPostIds(successSet);
        }

        return successSet.isEmpty();
    }

    /* TODO: Written by - Han Yongding 2024/07/08 根据postId获取点击量和版本号 */
    private PostHits getHitsAndVersionByPostId(String postId) {
        return mapper.selectPostHitsByPostId(postId);
    }

    /* TODO: Written by - Han Yongding 2024/07/08 异步更新 */
    private boolean asyncUpdatePostHits(List<PostHits> postHits) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        /* TODO: Written by - Han Yongding 2024/07/08 记录更新成功的postId，线程安全、不可重复 */
        ConcurrentSkipListSet<String> successSet = new ConcurrentSkipListSet<>();

        postHits.forEach(vo -> {
            /* TODO: Written by - Han Yongding 2024/07/08 异步更新 */
            CompletableFuture<Void> booleanCompletableFuture =
                    CompletableFuture.runAsync(() -> {
                        /* TODO: Written by - Han Yongding 2024/07/08 重试机制 */
                        boolean updated = false;
                        int maxRetry = 3;
                        int retryCount = 0;

                        /*
                         * 只重试3次，超过就抛弃,
                         * 理论上来说不需要重试，因为互不影响
                         */
                        while (!updated && retryCount < maxRetry) {
                            /* TODO: Written by - Han Yongding 2024/07/09 获取版本号和点击量 */
                            PostHits hitsAndVersionByPostId = this.getHitsAndVersionByPostId(vo.getPostId());
                            vo.setVersion(hitsAndVersionByPostId.getVersion()) ;
                            String hits = vo.getHits() ;
                            vo.setHits(String.valueOf(Integer.parseInt(vo.getHits()) + Integer.parseInt(hitsAndVersionByPostId.getHits())));

                            /* TODO: Written by - Han Yongding 2024/07/08 尝试更新 */
                            updated = !CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.updatePostHitsByPostId(vo));

                            /* TODO: Written by - Han Yongding 2024/07/08 更新失败 */
                            if (!updated) {
                                PostHits postHits1 = this.getHitsAndVersionByPostId(vo.getPostId());
                                vo.setVersion(postHits1.getVersion()) ;
                                vo.setHits(String.valueOf((Integer.parseInt(vo.getHits()) - Integer.parseInt(hits)) + Integer.parseInt(postHits1.getHits())));
                            }
                            /* TODO: Written by - Han Yongding 2024/07/08 计数 */
                            retryCount++;

                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                LogUtils.error(this.getClass(), "帖子点击量更新失败：" + e.getMessage());
                                /* TODO: Written by - Han Yongding 2024/07/08 终止线程 */
                                Thread.currentThread().interrupt();
                            }
                        }

                        /* TODO: Written by - Han Yongding 2024/07/08 更新成功，记录其唯一标识 */
                        if (updated) {
                            successSet.add(vo.getPostId());
                        }

                    });
            futures.add(booleanCompletableFuture);
        });

        /* TODO: Written by - Han Yongding 2024/07/08 等待全部任务完成 */
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        /* TODO: Written by - Han Yongding 2024/07/08 将需要更新的postId扔入队列，同步到ES */
        if (!successSet.isEmpty()) {
            this.synchronizationPostByPostIds(successSet);
        }

        return successSet.isEmpty();
    }

    /* TODO: Written by - Han Yongding 2024/07/08 同步ES */
    private void synchronizationPostByPostIds(ConcurrentSkipListSet<String> postIds) {
        postIds.forEach(postId -> {
            amqpTemplate.convertAndSend("postSyncES", postId);
        });
    }

    /* TODO: Written by - Han Yongding 2024/07/08 根据帖子唯一标识查询点击量表中是否存在 */
    private Boolean existPostHitsByPostId(String postId) {
        QueryWrapper<PostHits> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", postId);
        return this.exists(queryWrapper);
    }

    /* TODO: Written by - Han Yongding 2024/07/08 根据帖子ID获取最新的点击量数据 */
    private PostHits getLatestPostHitsByPostId(String postId) {
        QueryWrapper<PostHits> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", postId);
        return this.getOne(queryWrapper);
    }

    @Override
    public String getHitsByPostId(String postId) {
        String hits = mapper.getHitsByPostId(postId);
        return hits == null ? "0" : hits ;
    }
}
