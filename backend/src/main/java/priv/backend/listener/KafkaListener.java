package priv.backend.listener;


import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import priv.backend.domain.dto.UserPreferences;
import priv.backend.domain.mongo.dto.MongoType;
import priv.backend.domain.mongo.dto.MongoUserPreferences;
import org.springframework.data.mongodb.core.query.Query;
import priv.backend.service.system.Impl.UserPreferencesServiceImpl;
import priv.backend.util.KafkaProducerUtils;
import priv.backend.util.LogUtils;
import priv.backend.util.MongoUtils;
import priv.backend.util.TimeUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : Kafka消费者
 * @CreateDate :  2024-05-07 21:36
 */
@Component
public class KafkaListener {

    /* TODO: Written by - Han Yongding 2024/05/08 Mongo工具类 */
    private final MongoUtils mongoUtils;

    /* TODO: Written by - Han Yongding 2024/05/14 注入用户偏好业务层实现类 */
    private final UserPreferencesServiceImpl preferencesService ;

    public KafkaListener(KafkaProducerUtils kafkaProducerUtils,
                         MongoUtils mongoUtils,
                         UserPreferencesServiceImpl preferencesService) {
        this.mongoUtils = mongoUtils;
        this.preferencesService = preferencesService;
    }

    /* TODO: Written by - Han Yongding 2024/05/08 收集用户喜好 */
    @org.springframework.kafka.annotation.KafkaListener(topics = "collect_user_preferences", groupId = "collect-group")
    private void collectUserPreferences(MongoUserPreferences vo) {
        /* TODO: Written by - Han Yongding 2024/05/08 记录开始时间 */
        TimeUtils.start();
        /* TODO: Written by - Han Yongding 2024/05/08 收集用户数据，持久化到mongoDB */
        LogUtils.info(this.getClass(), "Collect-Kafka" + "——收集用户数据任务开始");
        /* TODO: Written by - Han Yongding 2024/05/08 准备处理数据 */
        LogUtils.info(this.getClass(), "Collect-Kafka" + "——开始处理数据");
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
        LogUtils.info(this.getClass(), "Collect-Kafka" + "——处理数据处理完成");
        LogUtils.info(this.getClass(), "Collect-Kafka" + "——准备存入MongoDB~");
        MongoUserPreferences save = mongoUtils.save(vo);
        if (save == null) {
            LogUtils.error(this.getClass(), "Collect-Kafka" + "——存入MongoDB失败~");
        } else {
            LogUtils.info(this.getClass(), "Collect-Kafka" + "——已存入MongoDB~");
        }
        /* TODO: Written by - Han Yongding 2024/05/08 计算同步耗时 */
        LogUtils.info(this.getClass(), "Collect-Kafka" + "——本次任务耗时" + TimeUtils.getCurrentTime(TimeUtils.getStartTime()));
        LogUtils.info(this.getClass(), "Collect-Kafka" + "——收集用户数据任务执行完毕");
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

    /* TODO: Written by - Han Yongding 2024/05/07 持久化处理好后的用户偏好数据 */
    @org.springframework.kafka.annotation.KafkaListener(topics = "analyze_and_persist_user_preferences", groupId = "persistence-group")
    public void persistentUserPreferences(UserPreferences vo) {
        this.insertUserPreferences(vo);
    }

    /* TODO: Written by - Han Yongding 2024/05/14 新增或修改 */
    private void insertUserPreferences(UserPreferences vo) {
        LogUtils.info(this.getClass(), "persistent-Kafka" + "——插入/更新用户偏好数据任务执行开始");
        /* TODO: Written by - Han Yongding 2024/05/14 记录开始时间 */
        TimeUtils.start();
        if (vo == null) {
            LogUtils.info(this.getClass(), "persistent-Kafka" + "——数据为空，不做处理");
            return;
        }
        /* TODO: Written by - Han Yongding 2024/05/14 正在插入 */
        LogUtils.info(this.getClass(), "persistent-Kafka" + "——正在插入/更新用户偏好数据");
        /* TODO: Written by - Han Yongding 2024/05/14 准备插入数据 */
        LogUtils.info(this.getClass(), "persistent-Kafka" + "——准备插入/更新数据");
        /* TODO: Written by - Han Yongding 2024/05/14 判断偏好表中是否存在用户 */
        UserPreferences userPreferences = preferencesService.selectByUserId(vo.getUserId());
        if (userPreferences == null) {
            /* TODO: Written by - Han Yongding 2024/05/14 用户不存在，插入数据 */
            if(preferencesService.insert(vo)) {
                LogUtils.info(this.getClass(), "persistent-Kafka" + "——插入成功");
            } else {
                LogUtils.info(this.getClass(), "persistent-Kafka" + "——插入失败");
            }
        } else {
            /* TODO: Written by - Han Yongding 2024/05/14 用户存在，更新数据 */
            vo.setPreferenceId(userPreferences.getPreferenceId());
            if (preferencesService.updateByPreferencesId(vo)) {
                LogUtils.info(this.getClass(), "persistent-Kafka" + "——更新成功");
            } else {
                LogUtils.info(this.getClass(), "persistent-Kafka" + "——更新失败");
            }
        }
        /* TODO: Written by - Han Yongding 2024/05/14 计算插入耗时 */
        LogUtils.info(this.getClass(), "persistent-Kafka" + "——本次任务耗时" + TimeUtils.getCurrentTime(TimeUtils.getStartTime()));
        LogUtils.info(this.getClass(), "persistent-Kafka" + "——插入/更新用户偏好数据任务执行完毕");
    }
}
