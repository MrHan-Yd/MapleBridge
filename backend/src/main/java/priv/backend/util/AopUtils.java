package priv.backend.util;

import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 作用
 * @CreateDate :  2024-06-10 17:00
 */
@Component
public class AopUtils {

    /* TODO: Written by - Han Yongding 2024/05/11 获取当前方法的Method对象 */
    public Method getCurrentMethod(JoinPoint joinPoint) {
        try {
            /* TODO: Written by - Han Yongding 2024/05/11 获取方法名 */
            String methodName = joinPoint.getSignature().getName();
            /* TODO: Written by - Han Yongding 2024/05/11 获取目标对象 */
            Object target = joinPoint.getTarget();
            /* TODO: Written by - Han Yongding 2024/05/11 获取目标对象的所有方法 */
            Method[] methods = target.getClass().getDeclaredMethods();
            /* TODO: Written by - Han Yongding 2024/05/11 遍历方法，找到与方法名匹配的方法 */
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    return method;
                }
            }
        } catch (Exception e) {
            LogUtils.error(this.getClass(), "获取当前方法失败");
        }
        return null;
    }
}
