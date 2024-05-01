package priv.backend.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 点赞表
 * @CreateDate :  2024-04-01 15:11
 */
@Data
@TableName("`like`")
public class Like {
    @TableId(type = IdType.ASSIGN_ID)
    private String likeId ;
    @TableField("user_id")
    private String userId ;
    @TableField("post_id")
    private String postId ;
    @TableField("comment_id")
    private String commentId ;
    @TableField("timestamp")
    private Date timestamp ;
}
