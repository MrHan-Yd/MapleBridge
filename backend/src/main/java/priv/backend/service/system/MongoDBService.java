package priv.backend.service.system;

import priv.backend.domain.MyEntity;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : MongoDB服务
 * @CreateDate :  2024-02-03 22:11
 */
public interface MongoDBService {
    void savaEntity(MyEntity entity) ;

    List<MyEntity> getAllUsers() ;
}
