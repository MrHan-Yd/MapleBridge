package priv.backend.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import priv.backend.domain.BaseData;

import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 帖子表
 * @CreateDate :  2024-03-08 15:03
 */
@Data
@TableName("post")
public class Post implements BaseData {
    @TableId(type = IdType.ASSIGN_ID)
    private String postId;
    @TableField("user_id")
    private String userId;
    @TableField("topic")
    private String topic;
    @TableField("content")
    private String content;
    @TableField("timestamp")
    private Timestamp timestamp;
    @TableField("type_id")
    private String typeId ;
    @TableField("like_count")
    private String likeCount;
    @TableField("comment_count")
    private String commentCount;
}
