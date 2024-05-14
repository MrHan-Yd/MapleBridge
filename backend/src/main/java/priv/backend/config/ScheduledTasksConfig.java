package priv.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import priv.backend.config.task.TaskExecutionPlan;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 每天零时为系统中的任务做执行计划
 * @CreateDate :  2024-05-14 15:12
 */
@Component
public class ScheduledTasksConfig {

    /* TODO: Written by - Han Yongding 2024/05/14 注入任务计划类 */
    private final TaskExecutionPlan taskExecutionPlan;

    @Autowired
    public ScheduledTasksConfig(TaskExecutionPlan taskExecutionPlan) {
        this.taskExecutionPlan = taskExecutionPlan;
    }
    /* TODO: Written by - Han Yongding 2024/05/14 每天零时执行一次任务 */
    @Scheduled(cron = "0 0 0 * * ?")
    public void executeTask() {
        /* TODO: Written by - Han Yongding 2024/05/14 动态对系统中的任务做执行计划 */
        taskExecutionPlan.scheduleTask();
    }
}
