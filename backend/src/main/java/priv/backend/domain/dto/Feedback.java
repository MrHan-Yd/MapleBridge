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
 * @Description : 反馈表DTO
 * @CreateDate :  2024-02-29 17:09
 */
@Data
@TableName("feedback")
public class Feedback implements BaseData {
    @TableId(type = IdType.ASSIGN_ID)
    private String feedbackId ;
    @TableField("user_id")
    private String userId ;
    @TableField("feedback_text")
    private String feedbackText ;
    @TableField("timestamp")
    private Timestamp timestamp ;
}
