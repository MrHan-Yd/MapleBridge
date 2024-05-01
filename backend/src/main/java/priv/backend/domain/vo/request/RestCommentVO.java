package priv.backend.domain.vo.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import priv.backend.domain.BaseData;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 接收前端评论实体类
 * @CreateDate :  2024-04-30 13:14
 */
@Data
public class RestCommentVO implements BaseData {
    @NotNull
    private String postId ;
    @NotNull
    private String userId ;
    @NotNull
    private String content ;
    private String commentId ;
    private String replyId ;
}
