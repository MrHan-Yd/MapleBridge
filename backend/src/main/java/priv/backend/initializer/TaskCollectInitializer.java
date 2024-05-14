package priv.backend.initializer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import priv.backend.config.task.TaskExecutionPlan;
import priv.backend.util.LogUtils;
import priv.backend.util.TimeUtils;


/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 收集任务
 * @CreateDate :  2024-05-11 12:44
 */

@Component
@Order(99)
@Slf4j
public class TaskCollectInitializer implements CommandLineRunner {
    /* TODO: Written by - Han Yongding 2024/05/11 注入任务执行计划类 */
    private final TaskExecutionPlan taskExecutionPlan;

    @Autowired
    public TaskCollectInitializer(TaskExecutionPlan taskExecutionPlan) {
        this.taskExecutionPlan = taskExecutionPlan;
    }


    /* TODO: Written by - Han Yongding 2024/05/11 收集任务 */
    @Override
    public void run(String... args){
        LogUtils.info(this.getClass(), "系统任务收集--开始");
        /* TODO: Written by - Han Yongding 2024/05/11 记录开始时间 */
        TimeUtils.start();
        LogUtils.info(this.getClass(), "系统任务收集--正在获取任务计划类中的所有任务");
        taskExecutionPlan.taskCollect();
        LogUtils.info(this.getClass(), "系统任务收集--处理任务计划类完毕");
        /* TODO: Written by - Han Yongding 2024/05/11 本次执行耗时 */
        LogUtils.info(this.getClass(),"系统任务收集--本次执行耗时" + TimeUtils.getCurrentTime(TimeUtils.getStartTime()));
        LogUtils.info(this.getClass(),"系统任务收集--结束");
    }

}
