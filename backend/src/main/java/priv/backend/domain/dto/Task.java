package priv.backend.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 任务表
 * @CreateDate :  2024-05-11 0:12
 */
@TableName("task")
@Data
public class Task {
    @TableId(type = IdType.ASSIGN_ID)
    private String taskId ;
    @TableField("task_name")
    private String taskName ;
    @TableField("task_remarks")
    private String taskRemarks ;
    @TableField("method_name")
    private String methodName ;
}
