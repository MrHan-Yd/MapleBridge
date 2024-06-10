package priv.backend.annotations.aspect;

import jakarta.annotation.Resource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletResponse;
import priv.backend.annotations.ExposeHeaders;
import priv.backend.enumeration.HeaderEnum;
import priv.backend.util.AopUtils;

import java.lang.reflect.Method;


/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 暴露请求头
 * @CreateDate :  2024-06-10 16:36
 */
@Aspect
@Component
public class ExposeHeadersAop {

    @Resource
    private AopUtils aopUtils ;

    /* 定义切点 */
    @Pointcut("@annotation(priv.backend.annotations.ExposeHeaders)")
    public void exposeHeaders() {

    }

    /* 开始之前切入 */
    @Before("exposeHeaders()")
    public void exposeHeadersAdvice(JoinPoint joinPoint) {
        /* 获取请求参数 */
        Object[] args = joinPoint.getArgs();
        System.out.println("切入了ExposeHeadersAop");

        /* 遍历 */
        for (Object arg : args) {
            /* 检查参数中是否有 HttpServletResponse 对象 */
            if (arg instanceof HttpServletResponse response) {
                /* 获取方法上的注解 */
                Method method = aopUtils.getCurrentMethod(joinPoint) ;
                /* 获取注解值 */
                String exposeHeaders = method.getAnnotation(ExposeHeaders.class).value() ;

                if (exposeHeaders != null) {
                    /* 设置响应头 */
                    response.setHeader(HeaderEnum.ACCESS_CONTROL_EXPOSE_HEADERS.headerName, exposeHeaders);
                }
            }
        }
    }
}
