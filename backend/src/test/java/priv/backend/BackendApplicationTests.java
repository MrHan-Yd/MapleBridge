package priv.backend;

import jakarta.annotation.Resource;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import priv.backend.config.task.UserPreferenceCollectionTask;
import priv.backend.config.task.TaskExecutionPlan;
import priv.backend.domain.PageBean;
import priv.backend.domain.mongo.dto.MongoType;
import priv.backend.domain.mongo.dto.MongoUserPreferences;
import priv.backend.enumeration.StatusEnum;
import priv.backend.util.CurrentUtils;

import java.security.SecureRandom;
import java.util.*;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(StatusEnum.NORMAL.STATE);
    }

    /* TODO: Written by - Han Yongding 2023/09/06 生成一个Security安全key */
    @Test
    void KeyGenerator() {
        // 示例，生成一个长度为32的安全密钥
        String key = generateSecurityKey(32);
        System.out.println(key);
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateSecurityKey(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    /* TODO: Written by - Han Yongding 2024/03/09 分页实体测试 */
    @Test
    void PageTest() {
        PageBean pageBean = new PageBean();
        System.out.println(pageBean);
    }

    /* TODO: Written by - Han Yongding 2024/04/15 版本更新 */
    @Test
    void selfIncreasing() {
        Double a = 0.9;
        System.out.println(CurrentUtils.versionUpdate(a));
    }

    /* TODO: Written by - Han Yongding 2024/04/15 点赞更新 */
    @Test
    void updateLikes() {
        Integer a = 1;
        System.out.println(CurrentUtils.countUpdate(a));
    }

    @Resource
    private UserPreferenceCollectionTask userPreferenceTasks;

    /* TODO: Written by - Han Yongding 2024/05/10 调用测试Task注解 */
//    @Test
//    void callTaskTest() {
//        userPreferenceTasks.executeTask() ;
//    }

    /* TODO: Written by - Han Yongding 2024/05/13 测试动态代理任务执行计划 */
//    @Test
//    void test() {
//        userPreferenceTasks.scheduleTask();
//    }

    /**
     * 使用基于内容的推荐算法推荐用户可能喜欢的帖子类型。
     *
     * @param userPreferencesList 包含用户点击信息的列表
     * @return 推荐结果，每个元素是一个 Recommendation 对象，包含用户ID和推荐的帖子类型列表
     */
    public List<Recommendation> recommend(List<MongoUserPreferences> userPreferencesList) {
        List<Recommendation> recommendations = new ArrayList<>();

        // 遍历用户点击信息列表，为每个用户推荐帖子类型
        for (MongoUserPreferences userPreferences : userPreferencesList) {
            String userId = userPreferences.getUserId();
            List<MongoType> typeList = userPreferences.getTypeList();

            // 创建最大堆来存储每个用户的点击类型，并按点击次数降序排列
            PriorityQueue<MongoType> maxHeap = new PriorityQueue<>(Comparator.comparingInt(MongoType::getCount).reversed());
            maxHeap.addAll(typeList);

            // 获取用户点击次数最多的两个帖子类型作为推荐结果
            List<String> recommendedTypes = new ArrayList<>();
            int count = 0;
            while (!maxHeap.isEmpty() && count < 2) {
                recommendedTypes.add(maxHeap.poll().getTypeId());
                count++;
            }

            // 将用户ID和推荐的帖子类型列表添加到推荐结果列表中
            recommendations.add(new Recommendation(userId, recommendedTypes));
        }

        return recommendations;
    }

    @Test
    void test2() {
        List<MongoUserPreferences> list = new ArrayList<>();

        // 添加第一个用户的点击信息
        MongoUserPreferences userPreferences1 = new MongoUserPreferences();
        userPreferences1.setUserId("12123");
        List<MongoType> typeList1 = new ArrayList<>();

        MongoType mongoType1 = new MongoType();
        mongoType1.setTypeId("1765981996910108674");
        mongoType1.setCount(15);
        typeList1.add(mongoType1);

        MongoType mongoType2 = new MongoType();
        mongoType2.setTypeId("1765982034910502913");
        mongoType2.setCount(6);
        typeList1.add(mongoType2);

        MongoType mongoType3 = new MongoType();
        mongoType3.setTypeId("1765981952316268545");
        mongoType3.setCount(10);
        typeList1.add(mongoType3);

        userPreferences1.setTypeList(typeList1);
        list.add(userPreferences1);

        // 添加第二个用户的点击信息
        MongoUserPreferences userPreferences2 = new MongoUserPreferences();
        userPreferences2.setUserId("23456");
        List<MongoType> typeList2 = new ArrayList<>();

        MongoType mongoType4 = new MongoType();
        mongoType4.setTypeId("1765981996910108674");
        mongoType4.setCount(10);
        typeList2.add(mongoType4);

        MongoType mongoType5 = new MongoType();
        mongoType5.setTypeId("1765982034910502913");
        mongoType5.setCount(16);
        typeList2.add(mongoType5);

        MongoType mongoType6 = new MongoType();
        mongoType6.setTypeId("1765981952316268545");
        mongoType6.setCount(30);
        typeList2.add(mongoType6);

        userPreferences2.setTypeList(typeList2);
        list.add(userPreferences2);

        List<Recommendation> recommendations = recommend(list);

        // 输出推荐结果
        for (Recommendation recommendation : recommendations) {
            System.out.println("User ID: " + recommendation.getUserId() + ", Recommended Types: " + recommendation.getRecommendedTypes());
        }
    }

    @Data
    class Recommendation {
        private String userId;
        private List<String> recommendedTypes;

        public Recommendation(String userId, List<String> recommendedTypes) {
            this.userId = userId;
            this.recommendedTypes = recommendedTypes;
        }

        // Getters
    }

    @Resource
    TaskExecutionPlan taskTaskManagement ;
    @Test
    void test3() {
        taskTaskManagement.scheduleTask() ;
    }

    /* TODO: Written by - Han Yongding 2024/05/14 测试从MongoDB中获取数据并处理 */
}
