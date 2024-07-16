package priv.backend.service.es.impl;

import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import priv.backend.domain.PageBean;
import priv.backend.domain.PostPage;
import priv.backend.domain.es.dto.ESPost;
import priv.backend.enumeration.MenuTypeEnum;
import priv.backend.repository.elasticsearch.ESPostRepository;
import priv.backend.service.es.ESPostService;
import priv.backend.service.system.Impl.UserPreferencesServiceImpl;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : ES Post业务层实现类
 * @CreateDate :  2024-04-04 16:52
 */
@Service
public class ESPostServiceImpl implements ESPostService {

    /* TODO: Written by - Han Yongding 2024/04/04 注入ES Post DAO层 */
    @Resource
    private ESPostRepository esPostRepository;

    /* TODO: Written by - Han Yongding 2024/07/08 注入用户偏好业务层 */
    @Resource
    private UserPreferencesServiceImpl userPreferencesService;

    /* TODO: Written by - Han Yongding 2024/07/12 注入客户端用户信息业务层实现类 */
    @Resource
    private ESClientUserServiceImpl clientUserService;


    /* TODO: Written by - Han Yongding 2024/04/04 查询所有帖子数据 */
    @Override
    public Page<ESPost> getAllESPost(PostPage page) {

        /* TODO: Written by - Han Yongding 2024/07/10 是推荐 */
        if (page.getType().equals(MenuTypeEnum.RECOMMEND.toString())) {
            Sort sort = Sort.by(Sort.Direction.DESC, "timestamp");
            Pageable pageable = PageRequest.of(page.getPage().getPageNum() - 1, page.getPage().getPageSize(), sort);
            /* TODO: Written by - Han Yongding 2024/07/10 如果是首页 */
            if (ObjectUtils.isEmpty(page.getMenuId())) {
                List<String> typeIds = userPreferencesService.selectPreferenceByUserId(page.getUserId()).getInterests();
                /* TODO: Written by - Han Yongding 2024/07/10 没有收集到偏好 */
                if (typeIds.isEmpty()) {
                    return esPostRepository.findAll(pageable);
                }
                /* TODO: Written by - Han Yongding 2024/07/10 按照偏好查询 */
                return esPostRepository.findByTypeTypeIdIn(typeIds, pageable);
            } else {
                /* TODO: Written by - Han Yongding 2024/07/10 其他分类，不需要偏好推荐 */
                return esPostRepository.findByTypeTypeId(page.getMenuId(), pageable);
            }
        } else {
            /* TODO: Written by - Han Yongding 2024/07/10 有问题!!!! */

            /* TODO: Written by - Han Yongding 2024/07/10 热门 */
            Sort sort = Sort.by(Sort.Direction.DESC, "views");
            Pageable pageable = PageRequest.of(page.getPage().getPageNum() - 1, page.getPage().getPageSize(), sort);
            /* TODO: Written by - Han Yongding 2024/07/10 如果是首页 */
            if (ObjectUtils.isEmpty(page.getMenuId())) {
                /* TODO: Written by - Han Yongding 2024/07/10 首页热门，不带偏好, 点击量不能为"0" */
                return esPostRepository.findByViewsIsNot("0", pageable);
            } else {
                /* TODO: Written by - Han Yongding 2024/07/10 其他分类，不需要偏好推荐 */
                return esPostRepository.findByTypeTypeIdAndViewsIsNot(page.getMenuId(), "0", pageable);
            }
        }
    }

    @Override
    public List<String> searchSuggest(String queryString) {
        if (ObjectUtils.isEmpty(queryString)) {
            return null;
        }

        queryString = queryString.trim();
        Sort sort = Sort.by(Sort.Direction.DESC, "timestamp");
        /* TODO: Written by - Han Yongding 2024/07/14 只查询最新的前八条 */
        Pageable pageable = PageRequest.of(0, 8, sort);
        List<ESPost> rs = esPostRepository.searchESPostsByTopicLike(queryString, pageable);
        List<CompletableFuture<Void>> task = new ArrayList<>();
        ConcurrentSkipListSet<String> data = new ConcurrentSkipListSet<>();
        rs.forEach(post -> {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                data.add(post.getTopic());
            });
            task.add(future);
        });
        CompletableFuture.allOf(task.toArray(new CompletableFuture[0])).join();

        return data.stream().toList();
    }

    @Override
    public Page<SearchHit<ESPost>> search(String queryString, PageBean pageBean) {
        if (ObjectUtils.isEmpty(queryString)) {
            return null;
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "timestamp");
        Pageable pageable = PageRequest.of(pageBean.getPageNum() - 1, pageBean.getPageSize(), sort);
        return esPostRepository.findByTopicOrContent(queryString, queryString, pageable);
    }
}
