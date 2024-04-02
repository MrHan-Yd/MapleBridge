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
 * @Description : 评论表DTO
 * @CreateDate :  2024-04-01 12:24
 */
@Data
@TableName("comment")
public class Comment implements BaseData {
    @TableId(type = IdType.ASSIGN_ID)
    private String commentId ;
    @TableField("post_id")
    private String postId ;
    @TableField("post_id")
    private String userId ;
    @TableField("content")
    private String content ;
    @TableField("timestamp")
    private Timestamp timestamp ;
    @TableField("like_count")
    private String likeCount ;

}
