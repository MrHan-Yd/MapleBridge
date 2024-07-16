package priv.backend.repository.elasticsearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import priv.backend.domain.es.dto.ESPost;

import javax.annotation.MatchesPattern;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : ES 帖子DAO
 * @CreateDate :  2024-03-31 21:40
 */
@Repository
public interface ESPostRepository extends ElasticsearchRepository<ESPost, String> {
    /* TODO: Written by - Han Yongding 2024/03/31 根据作用名称搜索 */
//    Page<ESPost> findByNickname(String name, Pageable pageable) ;
    /* TODO: Written by - Han Yongding 2024/03/31 搜索标题 */
    Page<ESPost> findByTopic(String title, Pageable pageable) ;
    /* TODO: Written by - Han Yongding 2024/03/31 内容包含标题 */
    Page<ESPost> findByTopicIsContaining(String word, Pageable pageable) ;

    /*
     * Written by - Han Yongding 2024/07/07
     * 根据typeId分页降序查询,偏好
     */
    Page<ESPost> findByTypeTypeIdIn(List<String> typeIds, Pageable pageable);

    /*
     * Written by - Han Yongding 2024/07/07
     * 根据typeId分页降序查询,子页推荐
     */
    Page<ESPost> findByTypeTypeId(String typeId, Pageable pageable);

    /* TODO: Written by - Han Yongding 2024/07/10 查询主页热门帖子 */
    Page<ESPost> findByViewsIsNot(String hits, Pageable pageable);
//
//    /* TODO: Written by - Han Yongding 2024/07/10 查询其他子页热门帖子 */
    Page<ESPost> findByTypeTypeIdAndViewsIsNot(String typeId, String hits, Pageable pageable);

    @Highlight(fields = {
            @HighlightField(name = "topic"),
            @HighlightField(name = "content")
    })
    Page<SearchHit<ESPost>> findByTopicOrContent(String topic, String content, Pageable pageable);


    List<ESPost> searchESPostsByTopicLike(String topic, Pageable pageable);
}
