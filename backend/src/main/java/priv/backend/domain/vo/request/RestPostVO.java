package priv.backend.domain.vo.request;

import lombok.Data;
import priv.backend.domain.BaseData;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 接收前端帖子VO
 * @CreateDate :  2024-03-09 21:34
 */
@Data
public class RestPostVO implements BaseData {
    private String postId ;
    private String userId ;
    private String topic ;
    private String content ;
    private String typeId ;
}
