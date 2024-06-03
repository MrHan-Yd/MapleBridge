package priv.backend.repository.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import priv.backend.domain.ServerParameters;


/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 服务器参数Redis访问层
 * @CreateDate :  2024-06-01 17:30
 */
@Repository
public interface ServerParametersRepository extends CrudRepository<ServerParameters, String> {
    @Override
    boolean existsById(@NonNull String id);
}
