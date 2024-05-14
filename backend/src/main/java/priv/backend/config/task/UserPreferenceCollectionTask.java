package priv.backend.config.task;

import jakarta.annotation.Resource;
import priv.backend.annotations.Task;
import priv.backend.annotations.TaskClass;
import priv.backend.domain.Recommendation;
import priv.backend.domain.dto.UserPreferences;
import priv.backend.domain.mongo.dto.MongoUserPreferences;
import priv.backend.enumeration.KafkaTopicEnum;
import priv.backend.util.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 处理用户偏好任务
 * @CreateDate :  2024-05-09 21:50
 */
@TaskClass
public class UserPreferenceCollectionTask {
    /* TODO: Written by - Han Yongding 2024/05/14 注入内容推荐工具类 */
    @Resource
    private RecommendUtil recommendUtil ;

    /* TODO: Written by - Han Yongding 2024/05/14 注入Mongo工具类 */
    @Resource
    private MongoUtils mongoUtils;

    /* TODO: Written by - Han Yongding 2024/05/14 注入Kafka工具类 */
    @Resource
    private KafkaProducerUtils kafkaProducerUtils;




    /* TODO: Written by - Han Yongding 2024/05/09 任务收集 */
    @Task(taskName = "user-preference-task", taskRemarks = "用户偏好处理任务")
    public void executeTask() {
        /* TODO: Written by - Han Yongding 2024/05/14 从mongo中查询前一天的数据 */
        List<MongoUserPreferences> query = mongoUtils.query(LocalDate.now().minusDays(1), MongoUserPreferences.class);
        /* TODO: Written by - Han Yongding 2024/05/14 将数据传递给内容推荐工具类，得到推荐结果 */
        List<Recommendation> recommend = recommendUtil.recommend(query);
        /* TODO: Written by - Han Yongding 2024/05/14 转换为UserPreferences并初始化其参数 */
        List<UserPreferences> userPreferencesList = recommend.stream().map(r -> {
            UserPreferences userPreferences = new UserPreferences();
            userPreferences.setUserId(r.getUserId());
            r.getRecommendedTypes().forEach(rt -> {
                if (userPreferences.getInterests1() == null) {
                    userPreferences.setInterests1(rt);
                } else {
                    userPreferences.setInterests2(rt);
                }
            });
            userPreferences.setLastUpdated(CurrentUtils.getTheCurrentSystemTime());
            return userPreferences;
        }).toList();
        /* TODO: Written by - Han Yongding 2024/05/14 遍历处理好的数据 */
        userPreferencesList.forEach(up -> {
            /* TODO: Written by - Han Yongding 2024/05/14 发送到Kafka中 */
            kafkaProducerUtils.sendMessage(KafkaTopicEnum.ANALYZE_AND_PERSIST_USER_PREFERENCES.topic, up) ;
        });
    }
}
