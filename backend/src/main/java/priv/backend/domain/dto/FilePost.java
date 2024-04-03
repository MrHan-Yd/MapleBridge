package priv.backend.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 帖子文件表
 * @CreateDate :  2024-04-03 12:23
 */
@Data
@TableName("file_post")
public class FilePost {
    @TableId(type = IdType.ASSIGN_ID)
    private String fileId ;
    @TableField("post_id")
    private String postId ;
    @TableField("file_type")
    private String fileType ;
    @TableField("file_name")
    private String fileName ;
    @TableField("file_suffix")
    private String fileSuffix ;
    @TableField("file_timestamp")
    private Timestamp fileTimestamp ;
}
