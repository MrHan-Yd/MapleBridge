package priv.backend.exception;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.util.validation.ValidationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
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
}
