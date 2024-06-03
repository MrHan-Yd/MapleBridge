package priv.backend.repository.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import priv.backend.domain.ServiceInformation;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 服务信息Redis访问层
 * @CreateDate :  2024-06-02 16:50
 */
@Repository
public interface ServiceInformationRepository extends CrudRepository<ServiceInformation, String> {

    @Override
    boolean existsById(@NonNull String id);
}
