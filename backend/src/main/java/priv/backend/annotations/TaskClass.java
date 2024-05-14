package priv.backend.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 任务类注解
 * @CreateDate :  2024-05-14 9:21
 */
@Target(ElementType.TYPE) // 注解作用于类上
@Retention(RetentionPolicy.RUNTIME) // 注解在运行时有效
@Component
public @interface TaskClass {
}
