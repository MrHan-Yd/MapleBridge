package priv.backend.util;

import org.springframework.stereotype.Component;
import priv.backend.domain.Recommendation;
import priv.backend.domain.mongo.dto.MongoType;
import priv.backend.domain.mongo.dto.MongoUserPreferences;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 内容推荐工具类
 * @CreateDate :  2024-05-14 21:36
 */
@Component
public class RecommendUtil {
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
}
