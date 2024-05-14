package priv.backend.annotations.aspect;


//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//import priv.backend.annotations.Task;
//import priv.backend.util.LogUtils;
//import priv.backend.util.TimeUtils;
//import java.lang.reflect.Method;

///**
// * Created by IntelliJ IDEA.
// *
// * @author : weiguang
// * @Description : 任务切面
// * @CreateDate :  2024-05-10 21:38
// */
//@Aspect // 声明一个切面
//@Component //切面放入Ioc容器中
//public class TaskAop {
//
//    /* TODO: Written by - Han Yongding 2024/05/10 定义切入点，匹配带有@Task注解的方法 */
//    @Pointcut("@annotation(priv.backend.annotations.Task)")
//    public void taskPointCut() {
//
//    }
//
//    /* TODO: Written by - Han Yongding 2024/05/10 方法执行后通知，方法执行后通知，获取@Task注解中的参数,前提是方法执行成功 */
//    @AfterReturning("taskPointCut()")
//    public void afterReturning(JoinPoint joinPoint) {
//        /* TODO: Written by - Han Yongding 2024/05/11 获取当前方法的Method对象 */
//        Method method = getCurrentMethod(joinPoint);
//        if (method != null) {
//            /* TODO: Written by - Han Yongding 2024/05/11 获取方法上的@Task注解  */
//            Task taskAnnotation = method.getAnnotation(Task.class);
//            if (taskAnnotation != null) {
//                /* TODO: Written by - Han Yongding 2024/05/11 获取@Task注解中的参数 */
//                String taskName = taskAnnotation.taskName();
//                String taskRemarks = taskAnnotation.taskRemarks();
//                System.out.println(taskName + "测试");
//                System.out.println(taskRemarks + "测试2");
//                /* TODO: Written by - Han Yongding 2024/05/11 记录开始时间 */
//                TimeUtils.start();
//                LogUtils.info(this.getClass(), "任务收集-切面已切入");
//                LogUtils.info(this.getClass(), "任务收集-任务收集开始");
//                System.out.println("任务切面执行成功！");
//                /* TODO: Written by - Han Yongding 2024/05/11 计算本次任务耗时 */
//                LogUtils.info(this.getClass(), "任务收集-本次任务耗时" + TimeUtils.getCurrentTime(TimeUtils.getStartTime()));
//                LogUtils.info(this.getClass(), "任务收集-任务收集结束");
//            }
//        }
//
//    }
//
//    /* TODO: Written by - Han Yongding 2024/05/11 获取当前方法的Method对象 */
//    private Method getCurrentMethod(JoinPoint joinPoint) {
//        try {
//            /* TODO: Written by - Han Yongding 2024/05/11 获取方法名 */
//            String methodName = joinPoint.getSignature().getName();
//            /* TODO: Written by - Han Yongding 2024/05/11 获取目标对象 */
//            Object target = joinPoint.getTarget();
//            /* TODO: Written by - Han Yongding 2024/05/11 获取目标对象的所有方法 */
//            Method[] methods = target.getClass().getDeclaredMethods();
//            /* TODO: Written by - Han Yongding 2024/05/11 遍历方法，找到与方法名匹配的方法 */
//            for (Method method : methods) {
//                if (method.getName().equals(methodName)) {
//                    return method;
//                }
//            }
//        } catch (Exception e) {
//            LogUtils.error(this.getClass(), "获取当前方法失败");
////            e.printStackTrace();
//        }
//        return null;
//    }
//}
