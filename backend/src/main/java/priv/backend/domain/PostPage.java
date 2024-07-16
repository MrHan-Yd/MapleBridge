package priv.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 帖子分页实体
 * @CreateDate :  2024-07-07 21:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostPage {
    private PageBean page ;
    private String userId ;
    private String type;
    private String menuId;
}
