package priv.backend.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : MongoDB工具类
 * @CreateDate :  2024-05-07 20:53
 */
@Component
public class MongoUtils {

    /* TODO: Written by - Han Yongding 2024/05/07 注入Mongo模板 */
    private final MongoTemplate template ;

    @Autowired
    public MongoUtils(MongoTemplate mongoTemplate) {
        this.template = mongoTemplate ;
    }

    /* TODO: Written by - Han Yongding 2024/05/07 保存数据 */
    public <T> T save(T data) {
        /* TODO: Written by - Han Yongding 2024/05/07 获取今天的时间日期 */
        String collectionName = LocalDate.now().toString() ;
        /* TODO: Written by - Han Yongding 2024/05/07 如果集合不存在则创建集合 */
        if (!template.collectionExists(collectionName)) {
            template.createCollection(collectionName) ;
        }
//        template.upsert(query, data, collectionName) ;
        /* TODO: Written by - Han Yongding 2024/05/07 保存数据 */
        return template.save(data, collectionName) ;
    }

    /* TODO: Written by - Han Yongding 2024/05/07 查询所有数据 */
    public <T> List<T> query(LocalDate date, Class<T> clazz) {
        /* TODO: Written by - Han Yongding 2024/05/07 根据时间日期获取集合名称 */
        String collectionName = date.toString() ;
        /* TODO: Written by - Han Yongding 2024/05/07 没有该集合，返回空 */
        if (!template.collectionExists(collectionName)) {
            return null ;
        }
        /* TODO: Written by - Han Yongding 2024/05/07 查询所有数据 */
        return template.findAll(clazz, collectionName) ;
    }

    /* TODO: Written by - Han Yongding 2024/05/07 根据用户ID查询数据 */
    public <T> T query(LocalDate date, Class<T> clazz, String userId) {
        /* TODO: Written by - Han Yongding 2024/05/07 根据时间日期获取集合名称 */
        String collectionName = date.toString() ;
        /* TODO: Written by - Han Yongding 2024/05/07 没有该集合，返回空 */
        if (!template.collectionExists(collectionName)) {
            return null ;
        }
        /* TODO: Written by - Han Yongding 2024/05/07 根据用户ID查询相应数据 */
        Query query = new Query(Criteria.where("userId").is(userId)) ;
        return template.findOne(query, clazz, collectionName) ;
    }

    /* TODO: Written by - Han Yongding 2024/05/07 根据用户ID查询数据 */
    public <T> T query(LocalDate date, Class<T> clazz, Query query) {
        /* TODO: Written by - Han Yongding 2024/05/07 根据时间日期获取集合名称 */
        String collectionName = date.toString() ;
        /* TODO: Written by - Han Yongding 2024/05/07 没有该集合，返回空 */
        if (!template.collectionExists(collectionName)) {
            return null ;
        }
        /* TODO: Written by - Han Yongding 2024/05/07 根据用户ID查询相应数据 */
        return template.findOne(query, clazz, collectionName) ;
    }
}
