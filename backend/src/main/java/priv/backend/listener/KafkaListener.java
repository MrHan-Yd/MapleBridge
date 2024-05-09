package priv.backend.listener;


import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import priv.backend.domain.mongo.dto.MongoType;
import priv.backend.domain.mongo.dto.MongoUserPreferences;
import org.springframework.data.mongodb.core.query.Query;
import priv.backend.util.KafkaProducerUtils;
import priv.backend.util.LogUtils;
import priv.backend.util.MongoUtils;
import priv.backend.util.TimeUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : Kafka消费者
 * @CreateDate :  2024-05-07 21:36
 */
@Component
public class KafkaListener {

    /* TODO: Written by - Han Yongding 2024/05/08 注入Kafka工具类 */
    private final KafkaProducerUtils utils;

    /* TODO: Written by - Han Yongding 2024/05/08 Mongo工具类 */
    private final MongoUtils mongoUtils;

    public KafkaListener(KafkaProducerUtils kafkaProducerUtils,
                         MongoUtils mongoUtils) {
        this.utils = kafkaProducerUtils;
        this.mongoUtils = mongoUtils;
    }

    /* TODO: Written by - Han Yongding 2024/05/07 收集用户喜好消费者1 */
    @org.springframework.kafka.annotation.KafkaListener(topics = "collect_user_preferences", groupId = "collect-group")
    public void collectUserPreferences1(MongoUserPreferences vo) {
        this.collectUserPreferences(vo, "Kafka-1");
    }

    /* TODO: Written by - Han Yongding 2024/05/07 收集用户喜好消费者2 */
    @org.springframework.kafka.annotation.KafkaListener(topics = "collect_user_preferences", groupId = "collect-group")
    public void collectUserPreferences2(MongoUserPreferences vo) {
        this.collectUserPreferences(vo, "Kafka-2");
    }

    /* TODO: Written by - Han Yongding 2024/05/08 收集用户喜好 */
    private void collectUserPreferences(MongoUserPreferences vo, String queueName) {
        /* TODO: Written by - Han Yongding 2024/05/08 记录开始时间 */
        TimeUtils.start();
        /* TODO: Written by - Han Yongding 2024/05/08 收集用户数据，持久化到mongoDB */
        LogUtils.info(this.getClass(), queueName + "——收集用户数据任务开始");
        /* TODO: Written by - Han Yongding 2024/05/08 准备处理数据 */
        LogUtils.info(this.getClass(), queueName + "——开始处理数据");
        /* TODO: Written by - Han Yongding 2024/05/09 构建查询条件，根据userId从MongoDB获取原始数据 */
        Query query = new Query(Criteria.where("_id").is(vo.getUserId()));
        /* TODO: Written by - Han Yongding 2024/05/08 从数据库中获取原始的count值 */
        MongoUserPreferences mongoUserPreferences = mongoUtils.query(LocalDate.now(), MongoUserPreferences.class, query);
        /* TODO: Written by - Han Yongding 2024/05/08 构建查询条件 */
        vo.getTypeList().forEach(l -> {
            /* TODO: Written by - Han Yongding 2024/05/09 从mongoDB中获取原始的list，并且遍历是否有相同的typeId */
            /* TODO: Written by - Han Yongding 2024/05/09 当天有收集过用户数据 */
            if (mongoUserPreferences != null) {
                List<MongoType> list = new ArrayList<>(mongoUserPreferences.getTypeList().stream().filter(
                        f -> f.getTypeId().equals(l.getTypeId())).toList());

                /* TODO: Written by - Han Yongding 2024/05/09 如果存在相同的typeId，则更新count值 */
                if (!list.isEmpty()) {
                    /* TODO: Written by - Han Yongding 2024/05/09 获取原list, 更新对应的count值 */
                    List<MongoType> typeList = mongoUserPreferences.getTypeList().stream().peek(t -> {
                        if (l.getTypeId().equals(t.getTypeId())) {
                            t.setCount(t.getCount() + l.getCount());
                        }
                    }).toList();
                    /* TODO: Written by - Han Yongding 2024/05/08 处理好的数据重新放入vo中 */
                    vo.setTypeList(typeList);
                } else {
                    /* TODO: Written by - Han Yongding 2024/05/09 当前没有收集过用户数据 */
                    List<MongoType> typeList = this.getTypes(mongoUserPreferences);
                    /* TODO: Written by - Han Yongding 2024/05/09 将新数据添加到list中 */
                    typeList.add(l);
                    /* TODO: Written by - Han Yongding 2024/05/08 处理好的数据重新放入vo中 */
                    vo.setTypeList(typeList);
                }

            }
        });
        LogUtils.info(this.getClass(), queueName + "——处理数据处理完成");
        LogUtils.info(this.getClass(), queueName + "——准备存入MongoDB~");
        MongoUserPreferences save = mongoUtils.save(vo);
        if (save == null) {
            LogUtils.error(this.getClass(), queueName + "——存入MongoDB失败~");
        } else {
            LogUtils.info(this.getClass(), queueName + "——已存入MongoDB~");
        }
        /* TODO: Written by - Han Yongding 2024/05/08 计算同步耗时 */
        LogUtils.info(this.getClass(), queueName + "——本次任务耗时" + TimeUtils.getCurrentTime(TimeUtils.getStartTime()));
        LogUtils.info(this.getClass(), queueName + "——收集用户数据任务执行完毕");
    }

    /* TODO: Written by - Han Yongding 2024/05/09 创建或沿用types */
    private List<MongoType> getTypes(MongoUserPreferences mongoUserPreferences) {
        List<MongoType> typeList;
        /* TODO: Written by - Han Yongding 2024/05/09 如果用户没有其他数据，则新创建一个list */
        if (mongoUserPreferences.getTypeList() == null) {
            typeList = new ArrayList<>();
        } else {
            /* TODO: Written by - Han Yongding 2024/05/09 用户有其他数据，需沿用原List，不能覆盖 */
            typeList = mongoUserPreferences.getTypeList();
        }
        return typeList;
    }

    /* TODO: Written by - Han Yongding 2024/05/07 持久化处理好后的用户偏好数据1 */
    @org.springframework.kafka.annotation.KafkaListener(topics = "analyze_and_persist_user_preferences", groupId = "persistence-group")

    public void persistentUserPreferences1(MongoUserPreferences vo) {
        System.out.println("Received message: " + vo);
    }

    /* TODO: Written by - Han Yongding 2024/05/07 持久化处理好后的用户偏好数据2 */
    @org.springframework.kafka.annotation.KafkaListener(topics = "analyze_and_persist_user_preferences", groupId = "persistence-group")

    public void persistentUserPreferences2(MongoUserPreferences vo) {
        System.out.println("Received message: " + vo);
    }
}
