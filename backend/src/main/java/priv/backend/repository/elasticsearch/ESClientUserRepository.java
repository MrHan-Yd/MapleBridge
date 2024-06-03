package priv.backend.repository.elasticsearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

}
