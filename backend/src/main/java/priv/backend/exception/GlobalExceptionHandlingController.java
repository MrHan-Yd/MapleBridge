package priv.backend.exception;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.util.validation.ValidationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import priv.backend.domain.RestBean;
import priv.backend.enumeration.CodeEnum;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 全局异常处理控制器
 * @CreateDate :  2023-09-17 22:34
 */
@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE) // 设置优先级最高
public class GlobalExceptionHandlingController {

    /* TODO: Written by - Han Yongding 2024/03/05 处理未授权异常 */
    @ExceptionHandler(AccessDeniedException.class)
    public RestBean<Void> handleUnauthorizedException(AccessDeniedException exception) {
        log.warn("Resolve [{}: {}]", exception.getClass().getName(), exception.getMessage());
        return RestBean.failure(CodeEnum.HTTP_401_UNAUTHORIZED.CODE, "未授权访问");
    }

    /** TODO: Written by - Han Yongding 2023/08/14 处理接口参数校验器异常 */
    @ExceptionHandler(ValidationException.class)
    public RestBean<Void> validateException(ValidationException exception) {
        log.warn("Resolve [{}: {}]", exception.getClass().getName(), exception.getMessage());
        return RestBean.failure(CodeEnum.HTTP_400_ERROR_REQUEST.CODE, "请求参数有误") ;
    }

    /** TODO: Written by - Han Yongding 2024/03/09 无效请求，可能是参数不正确或请求语法错误，服务器无法理解 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestBean<Void> MethodArgumentNotValid(MethodArgumentNotValidException exception){
        log.warn("Resolve [{}: {}]", exception.getClass().getName(), exception.getMessage());
        return RestBean.failure(CodeEnum.HTTP_400_ERROR_REQUEST.CODE, CodeEnum.HTTP_400_ERROR_REQUEST.MESSAGE);
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

    /** TODO: Written by - Han Yongding 2024/03/11 令牌签名无效 */
    @ExceptionHandler(SignatureVerificationException.class)
    public RestBean<Void> signatureVerificationException(SignatureVerificationException exception){
        log.warn("Resolve [{}: {}]", exception.getClass().getName(), exception.getMessage());
        return RestBean.failure(CodeEnum.HTTP_500_INTERNAL_SERVER_ERROR.CODE, "签名验证异常");
    }

    /** TODO: Written by - Han Yongding 2024/03/11 令牌签名无效 */
    @ExceptionHandler(JWTDecodeException.class)
    public RestBean<Void> JWTDecodeException(JWTDecodeException exception){
        log.warn("Resolve [{}: {}]", exception.getClass().getName(), exception.getMessage());
        return RestBean.failure(CodeEnum.HTTP_400_ERROR_REQUEST.CODE, "解码异常");
    }

    /** TODO: Written by - Han Yongding 2024/04/02 上传文件超出大小 */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public RestBean<Void> UploadFileGoBeyondSizeException(MaxUploadSizeExceededException exception) {
        log.warn("Resolve [{}: {}]", exception.getClass().getName(), exception.getMessage());
        return RestBean.failure(CodeEnum.HTTP_400_ERROR_REQUEST.CODE, "已超出最大上传文件大小");
    }

    /* TODO: Written by - Han Yongding 2024/03/26 无法连接到redis */
    @ExceptionHandler({RedisConnectionFailureException.class})
    public RestBean<Void> handleRedisAndDatabaseExceptions(RedisConnectionFailureException exception) {
        log.warn("Resolve [{}: {}]", exception.getClass().getName(), exception.getMessage());
        return RestBean.failure(CodeEnum.HTTP_500_INTERNAL_SERVER_ERROR.CODE, "Redis服务无法连接，请联系管理员处理");
    }

    /* TODO: Written by - Han Yongding 2024/03/05 处理通用异常 */
    @ExceptionHandler(Exception.class)
    public RestBean<Void> exception(Exception exception) {
        log.warn("Resolve [{}: {}]", exception.getClass().getName() + "-" + Arrays.toString(exception.getStackTrace()), exception.getMessage());
        return RestBean.failure(CodeEnum.HTTP_500_INTERNAL_SERVER_ERROR.CODE, "服务器内部错误，请联系管理员");
    }

    /* TODO: 编写者 - 您的姓名 2024/05/29 处理Redis异常 */
    /* TODO: Written by - Han Yongding 2024/05/29 处理Redis异常 */
    @ExceptionHandler(org.springframework.dao.InvalidDataAccessApiUsageException.class)
    public RestBean<Void> handleRedisException(org.springframework.dao.InvalidDataAccessApiUsageException exception) {
        log.warn("Resolve [{}: {}]", exception.getClass().getName(), exception.getMessage());
        // 根据具体情况返回适当的错误信息或状态码
        return RestBean.failure(CodeEnum.HTTP_500_INTERNAL_SERVER_ERROR.CODE, "Redis配置错误或服务异常关闭，请检查Redis日志获取详细信息");
    }

}
