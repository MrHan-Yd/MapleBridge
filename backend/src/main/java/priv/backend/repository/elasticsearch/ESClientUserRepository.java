package priv.backend.repository.elasticsearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import priv.backend.domain.es.dto.ESClientUser;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : ES 客户端用户数据DAO
 * @CreateDate :  2024-04-07 10:18
 */
@Repository
public interface ESClientUserRepository extends ElasticsearchRepository<ESClientUser, String> {

    /* TODO: Written by - Han Yongding 2024/07/12 根据昵称查询用户 */
    @HighlightField(name = "nickname")
    Page<SearchHit<ESClientUser>> findByNickname(String nickname, Pageable pageable);
}
