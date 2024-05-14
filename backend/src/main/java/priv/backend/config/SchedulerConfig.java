package priv.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 轻量级多线程定时任务框架配置类
 * @CreateDate :  2024-05-09 21:37
 */
@Configuration
public class SchedulerConfig {

    /* TODO: Written by - Han Yongding 2024/05/14 任务调度池大小 */
    @Value("${spring.thread-pool-task-scheduler.pool-size}")
    private int poolSize;

    /* TODO: Written by - Han Yongding 2024/05/14 任务调度线程池前缀  */
    @Value("${spring.thread-pool-task-scheduler.thread-name-prefix}")
    private String threadNamePrefix;

    /* TODO: Written by - Han Yongding 2024/05/14 项目包路径 */
    @Value("${spring.thread-pool-task-scheduler.project-package-path}")
    private String projectPackagePath;

    /* TODO: Written by - Han Yongding 2024/05/09 定时任务线程池Bean */
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        /* TODO: Written by - Han Yongding 2024/05/09 线程池大小 */
        scheduler.setPoolSize(poolSize);
        /* TODO: Written by - Han Yongding 2024/05/09 线程池前缀 */
        scheduler.setThreadNamePrefix(threadNamePrefix);
        scheduler.initialize();
        return scheduler;
    }

    /* TODO: Written by - Han Yongding 2024/05/14 项目包路径 */
    @Bean("ProjectPackagePath")
    public String projectPackagePath() {
        return this.projectPackagePath ;
    }
}
