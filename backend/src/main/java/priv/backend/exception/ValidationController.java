package priv.backend.exception;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.util.validation.ValidationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import priv.backend.domain.RestBean;
import priv.backend.enumeration.CodeEnum;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 全局异常处理控制器
 * @CreateDate :  2023-09-17 22:34
 */
@Slf4j
@RestControllerAdvice
public class ValidationController {
    /** TODO: Written by - Han Yongding 2023/08/14 处理接口参数校验器异常 */
    @ExceptionHandler(ValidationException.class)
    public RestBean<Void> validateException(ValidationException exception) {
        log.warn("Resolve [{}: {}]", exception.getClass().getName(), exception.getMessage());
        return RestBean.failure(CodeEnum.HTTP_401_UNAUTHORIZED.CODE, "请求参数有误") ;
    }

    /** TODO: Written by - Han Yongding 2024/03/05 请求方法不支持 */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public RestBean<Void> requestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        log.warn("Resolve [{}: {}]", exception.getClass().getName(), exception.getMessage());
        return RestBean.failure(CodeEnum.HTTP_405_METHOD_NOT_ALLOWED.CODE, "请求方法不支持");
    }

    /* TODO: Written by - Han Yongding 2024/03/05 请求资源未找到 */
    @ExceptionHandler(NoHandlerFoundException.class)
    public RestBean<Void> noHandlerFoundException(NoHandlerFoundException exception) {
        log.warn("Resolve [{}: {}]", exception.getClass().getName(), exception.getMessage());
        return RestBean.failure(CodeEnum.HTTP_404_RESOURCE_NOT_FOUND.CODE, "请求资源未找到");
    }

    /* TODO: Written by - Han Yongding 2024/03/05 处理通用异常 */
    @ExceptionHandler(Exception.class)
    public RestBean<Void> exception(Exception exception) {
        log.warn("Resolve [{}: {}]", exception.getClass().getName(), exception.getMessage());
        return RestBean.failure(CodeEnum.HTTP_500_INTERNAL_SERVER_ERROR.CODE, "服务器内部错误，请联系管理员");
    }
}
