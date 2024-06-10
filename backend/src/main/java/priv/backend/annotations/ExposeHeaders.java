package priv.backend.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 暴露header注解
 * @CreateDate :  2024-06-10 16:33
 */
@Target(ElementType.METHOD) // 注解作用于方法上
@Retention(RetentionPolicy.RUNTIME) //保留注解到运行时
public @interface ExposeHeaders {
    String value(); // 要暴露的header名称
}
