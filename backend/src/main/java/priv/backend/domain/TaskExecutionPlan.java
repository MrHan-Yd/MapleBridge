package priv.backend.domain;

import lombok.Data;
import priv.backend.domain.dto.Task;


/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 任务执行计划
 * @CreateDate :  2024-05-13 19:38
 */
@Data
public class TaskExecutionPlan {
    private String id ;
    private Task task ;
    private String cron ;
    private String remarks ;
    private String status ;
}
