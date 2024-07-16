package priv.backend.domain.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 接收前端帖子点击量实体
 * @CreateDate :  2024-07-09 11:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestPostHits {
    private String postId;
    private String hits;
}
