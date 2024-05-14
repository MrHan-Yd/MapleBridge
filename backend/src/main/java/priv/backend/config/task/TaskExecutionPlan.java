package priv.backend.config.task;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import priv.backend.annotations.Task;
import priv.backend.annotations.TaskClass;
import priv.backend.service.system.Impl.TaskPlanServiceImpl;
import priv.backend.service.system.Impl.TaskServiceImpl;
import priv.backend.util.LogUtils;
import priv.backend.util.TimeUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 系统任务执行计划(动态定时任务)
 * @CreateDate :  2024-05-14 13:02
 */
@Component
public class TaskExecutionPlan {

    /* TODO: Written by - Han Yongding 2024/05/09 任务调度器 */
    private final TaskScheduler taskScheduler;
    /* TODO: Written by - Han Yongding 2024/05/09 计划 */
    private final Map<String, ScheduledFuture<?>> futureTaskMap = new HashMap<>();

    /* TODO: Written by - Han Yongding 2024/05/11 注入计划表业务层实现类 */
    private final TaskServiceImpl taskService;

    /* TODO: Written by - Han Yongding 2024/05/13 注入任务计划执行表业务层实现类 */
    private final TaskPlanServiceImpl taskPlanService;

    /* TODO: Written by - Han Yongding 2024/05/14 任务列表类集合 */
    private final List<Class<?>> taskList = new ArrayList<>();

    /* TODO: Written by - Han Yongding 2024/05/14 收集代码中的任务集合 */
    List<String> list = new ArrayList<>();
    /* TODO: Written by - Han Yongding 2024/05/14 数据库中已开启或有计划的的任务 */
    List<priv.backend.domain.TaskExecutionPlan> taskPlan = new ArrayList<>();

    /* TODO: Written by - Han Yongding 2024/05/14 项目包路径 */
    private final String projectPackagePath;

    @Autowired
    public TaskExecutionPlan(TaskScheduler taskScheduler,
                             TaskServiceImpl taskService,
                             TaskPlanServiceImpl taskPlanService,
                             String projectPackagePath) {
        this.taskScheduler = taskScheduler;
        this.taskService = taskService;
        this.taskPlanService = taskPlanService;
        this.projectPackagePath = projectPackagePath;

        /* TODO: Written by - Han Yongding 2024/05/14 扫描 */
        this.scanning();
    }

    /* TODO: Written by - Han Yongding 2024/05/14 扫描项目中的任务类 */
    public void scanning() {
        /* TODO: Written by - Han Yongding 2024/05/14 设置 Reflections 扫描器 */
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackages(projectPackagePath)
                .addScanners(new TypeAnnotationsScanner(), new SubTypesScanner()));

        /* TODO: Written by - Han Yongding 2024/05/14 找到标记有 @TaskClass 注解的类 */
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(TaskClass.class);

        /* TODO: Written by - Han Yongding 2024/05/14 收集所有标记有 @TaskClass 注解的类 */
        taskList.addAll(annotatedClasses);
    }

    /* TODO: Written by - Han Yongding 2024/05/09 任务执行计划 */
    public void scheduleTask() {
        /* TODO: Written by - Han Yongding 2024/05/14 遍历任务列表类集合 */
        taskList.forEach(tl -> {
            /* TODO: Written by - Han Yongding 2024/05/13 记录开始时间 */
            TimeUtils.start();
            LogUtils.info(this.getClass(), "系统任务执行计划--开始");
            LogUtils.info(this.getClass(), "系统任务执行计划--获取任务执行计划类中所有方法");
            /* TODO: Written by - Han Yongding 2024/05/13 反射获取当前类中的所有方法 */
            Method[] methods = tl.getDeclaredMethods();
            LogUtils.info(this.getClass(), "系统任务执行计划--获取数据库中所有任务及执行计划");

            /* TODO: Written by - Han Yongding 2024/05/13 遍历当前类中的所有方法 */
            LogUtils.info(this.getClass(), "系统任务执行计划--开始遍历任务执行计划类中所有方法");
            for (Method method : methods) {
                /* TODO: Written by - Han Yongding 2024/05/13 获取当前方法的 Task 注解信息 */
                Task taskAnnotation = method.getAnnotation(Task.class);
                /* TODO: Written by - Han Yongding 2024/05/13 如果当前方法上有 Task 注解 */
                if (taskAnnotation != null) {
                    LogUtils.info(this.getClass(), "系统任务执行计划--找到带任务注解的方法");
                    /* TODO: Written by - Han Yongding 2024/05/13 获取当前方法的任务对象 */
                    priv.backend.domain.dto.Task task = this.getTask(method, taskAnnotation);
                    LogUtils.info(this.getClass(), "系统任务执行计划--获取带任务注解方法的任务名和备注");
                    /* TODO: Written by - Han Yongding 2024/05/13 遍历任务执行计划表 */
                    LogUtils.info(this.getClass(), "系统任务执行计划--遍历任务执行计划表已开启的执行计划");
                    /* TODO: Written by - Han Yongding 2024/05/13 从数据库中查询开启的任务计划以及列表 */
                    taskPlan = taskPlanService.getTaskPlan();
                    /* TODO: Written by - Han Yongding 2024/05/14 任务计划 */
                    this.taskPlan(taskPlan, task.getTaskName(), task.getMethodName(),tl, method);
                }
            }
            LogUtils.info(this.getClass(), "系统任务执行计划--系统任务执行计划制定完成");
            LogUtils.info(this.getClass(), "系统任务执行计划--本次执行耗时" + TimeUtils.getCurrentTime(TimeUtils.getStartTime()));
            LogUtils.info(this.getClass(), "系统任务执行计划--本次任务结束");
        });
    }

    /* TODO: Written by - Han Yongding 2024/05/14 任务计划 */

    public void taskPlan(List<priv.backend.domain.TaskExecutionPlan> taskPlan, String taskName, String methodName, Class<?> tl, Method method) {
        taskPlan
                .stream()
                /* TODO: Written by - Han Yongding 2024/05/13 过滤任务计划表中符合当前代码中的任务  */
                .filter(f ->
                        f.getTask().getTaskName().equals(taskName)
                                && f.getTask().getMethodName().equals(methodName))
                .forEach(s -> {
                    LogUtils.info(this.getClass(), "系统任务执行计划--找到任务执行计划表在系统中对应的任务");
                    /* TODO: Written by - Han Yongding 2024/05/13 按照计划执行任务 */

                    LogUtils.info(this.getClass(), "系统任务执行计划--开始安排执行任务的计划");
                    /* TODO: Written by - Han Yongding 2024/05/13 使用 taskScheduler.schedule 方法来安排执行任务，并将返回的 FutureTask 对象存储在 futureTask 中 */
                    Runnable runnable = () -> {
                        try {
                            /* TODO: Written by - Han Yongding 2024/05/14 通过反射获取当前类的实例，并传递给当前实例需要执行的方法 */
                            method.invoke(tl.getConstructor().newInstance()) ;
                        } catch (IllegalAccessException | InvocationTargetException |
                                 InstantiationException | NoSuchMethodException e) {
                            throw new RuntimeException(e);
                        }
                    } ;
                    /* TODO: Written by - Han Yongding 2024/05/14 如果是开启状态，则添加任务，否则取消 */
                    if ("0".equals(s.getStatus())) {
                        /* TODO: Written by - Han Yongding 2024/05/14 添加任务 */
                        this.addTask(runnable,s.getCron() , s.getTask().getTaskName());
                    } else {
                        /* TODO: Written by - Han Yongding 2024/05/14 取消任务 */
                        this.cancelTaskByName(s.getTask().getTaskName());
                    }
                });
    }

    /* TODO: Written by - Han Yongding 2024/05/14 向定时任务列表中添加任务 */
    private void addTask(Runnable task, String cron, String taskName) {
        /* TODO: Written by - Han Yongding 2024/05/14 检查任务名称是否已存在 */
        if (futureTaskMap.containsKey(taskName)) {
            /* 如果任务名称已存在，先取消现有任务 */
            ScheduledFuture<?> existingFuture = futureTaskMap.get(taskName);
            existingFuture.cancel(true);
        }

        /* TODO: Written by - Han Yongding 2024/05/14 调度新的任务，并将其添加到定时任务列表和任务名称映射中 */
        ScheduledFuture<?> futureTask = taskScheduler.schedule(task, new CronTrigger(cron));
        /* 将任务名称和ScheduledFuture对象存储到futureTaskMap中 */
        futureTaskMap.put(taskName, futureTask);
    }
    /* TODO: Written by - Han Yongding 2024/05/14 根据任务名称取消 */
    private void cancelTaskByName(String taskName) {
        ScheduledFuture<?> future = futureTaskMap.get(taskName);
        if (future != null) {
            /* 取消任务的执行 */
            future.cancel(true);
            /* 从任务名称映射中移除任务 */
            futureTaskMap.remove(taskName);
        }
    }

    /* TODO: Written by - Han Yongding 2024/05/14 取消所有 */
    private void cancelAllTasks() {
        for (ScheduledFuture<?> future : futureTaskMap.values()) {
            future.cancel(true);
        }
        futureTaskMap.clear(); // 清空任务名称映射
    }

    /* TODO: Written by - Han Yongding 2024/05/11 任务收集  */
    public void taskCollect() {
        /* TODO: Written by - Han Yongding 2024/05/14 遍历任务列表类集合 */
        taskList.forEach(tl -> {
            /* TODO: Written by - Han Yongding 2024/05/11 获取任务计划类中的所有任务 */
            Method[] methods = tl.getDeclaredMethods();

            LogUtils.info(this.getClass(), "系统任务收集--开始处理任务计划类中携带注解的任务");
            /* TODO: Written by - Han Yongding 2024/05/11 开始处理数据 */
            for (Method method : methods) {
                /* TODO: Written by - Han Yongding 2024/05/11 获取本类中间使用了Task注解的方法 */
                Task taskAnnotation = method.getAnnotation(Task.class);
                if (taskAnnotation != null) {
                    /* TODO: Written by - Han Yongding 2024/05/11 封装对象 */
                    priv.backend.domain.dto.Task task = this.getTask(method, taskAnnotation);
                    list.add(task.getTaskName());
                    if (taskService.selectTaskByTaskName(task.getTaskName()) != null) {
                        /* TODO: Written by - Han Yongding 2024/05/11 更新 */
                        if (taskService.updateTaskByTaskName(task)) {
                            LogUtils.info(this.getClass(), "系统任务收集--更新任务" + task.getTaskName() + "成功");
                        } else {
                            LogUtils.error(this.getClass(), "系统任务收集--更新任务" + task.getTaskName() + "失败");
                        }
                    } else {
                        /* TODO: Written by - Han Yongding 2024/05/11 插入 */
                        if (taskService.insertOrUpdateTask(task)) {
                            LogUtils.info(this.getClass(), "系统任务收集--插入任务" + task.getTaskName() + "成功");
                        } else {
                            LogUtils.info(this.getClass(), "系统任务收集--插入任务" + task.getTaskName() + "失败");
                        }
                    }
                }
            }
        });
        /* TODO: Written by - Han Yongding 2024/05/11 删除数据库中不存在于列表中的数据  */
        for (priv.backend.domain.dto.Task task1 : taskService.list()) {
            /* TODO: Written by - Han Yongding 2024/05/11 没有在列表中的任务 */
            if (!list.contains(task1.getTaskName())) {
                /* TODO: Written by - Han Yongding 2024/05/11 删除 */
                taskService.deleteTaskByTaskName(task1.getTaskName());
                /* TODO: Written by - Han Yongding 2024/05/15 删除其任务执行计划 */
                taskPlanService.deleteTaskPlanByTaskId(task1.getTaskId()) ;
            }
        }
    }

    /* TODO: Written by - Han Yongding 2024/05/11 封装任务对象  */
    private priv.backend.domain.dto.Task getTask(Method method, Task taskAnnotation) {
        priv.backend.domain.dto.Task task = new priv.backend.domain.dto.Task();
        /* TODO: Written by - Han Yongding 2024/05/11 方法名 */
        task.setMethodName(method.getName());
        /* TODO: Written by - Han Yongding 2024/05/11 任务名  */
        task.setTaskName(taskAnnotation.taskName());
        /* TODO: Written by - Han Yongding 2024/05/11 任务描述 */
        task.setTaskRemarks(taskAnnotation.taskRemarks());
        return task;
    }

}
