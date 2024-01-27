package priv.backend.service.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import priv.backend.domain.MyEntity;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : MongoDB服务
 * @CreateDate :  2024-01-09 21:58
 */
@Service
public class MongoDBService {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoDBService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void savaEntity(MyEntity entity) {
        mongoTemplate.save(entity) ;
    }

    public List<MyEntity> getAllUsers() {
        // 创建一个Query对象以指定查询条件
        Query query = new Query();

        // 使用find方法执行查询并检索结果
        List<MyEntity> users = mongoTemplate.find(query, MyEntity.class);

        return users;
    }
}
