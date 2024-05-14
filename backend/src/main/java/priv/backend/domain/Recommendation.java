package priv.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 内容推荐实体类
 * @CreateDate :  2024-05-14 21:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recommendation {
    private String userId;
    private List<String> recommendedTypes;

}
