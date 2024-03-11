package priv.backend.domain.vo.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 响应前端的帖子VO
 * @CreateDate :  2024-03-09 17:55
 */
@Data
public class RespPostVO {
    private String postId;
    private String userId;
    private String topic;
    private String content;
    private Timestamp timestamp;
    private RespTypesPostSelectVO types ;
    private String likeCount;
    private String commentCount;
}
