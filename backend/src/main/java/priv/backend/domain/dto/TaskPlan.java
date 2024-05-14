package priv.backend.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import priv.backend.domain.BaseData;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 任务执行计划表
 * @CreateDate :  2024-05-11 16:50
 */
@TableName("task_plan")
@Data
public class TaskPlan implements BaseData {
    @TableId(type = IdType.ASSIGN_ID)
    private String id ;
    @TableField("task_id")
    private String taskId ;
    @TableField("cron")
    private String cron ;
    @TableField("status")
    private String status ;
    @TableField("remarks")
    private String remarks ;
    @TableField("create_id")
    private String createId ;
    @TableField("create_time")
    private Date createTime ;
    @TableField("update_id")
    private String updateId ;
    @TableField("update_time")
    private Date updateTime ;
}
