package priv.backend.repository.elasticsearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import priv.backend.domain.es.dto.ESPost;

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

}
