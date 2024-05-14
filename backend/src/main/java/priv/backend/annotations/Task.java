package priv.backend.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 任务注解
 * @CreateDate :  2024-05-10 21:06
 */
@Target(ElementType.METHOD) // 注解作用于方法上
@Retention(RetentionPolicy.RUNTIME) //保留注解到运行时
public @interface Task {
    /* TODO: Written by - Han Yongding 2024/05/10 任务名称 */
    String taskName() ;
    /* TODO: Written by - Han Yongding 2024/05/10 任务备注 */
    String taskRemarks() ;
}
