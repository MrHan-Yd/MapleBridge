package priv.backend.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 关键词DTO
 * @CreateDate :  2024-06-30 13:34
 */
@TableName("sensitive_words")
@Data
public class SensitiveWords {
    @TableId(type = IdType.ASSIGN_ID)
    private String id ;
    @TableField("word")
    private String word;
    @TableField("create_id")
    private String createId;
    @TableField("create_time")
    private LocalDateTime createTime;
}
