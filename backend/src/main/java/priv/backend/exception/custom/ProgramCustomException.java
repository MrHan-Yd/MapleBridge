package priv.backend.exception.custom;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 程序自定义异常，通用异常类
 * @CreateDate :  2023-11-01 10:05
 */
public class ProgramCustomException extends Exception{

    private String message ;

    public ProgramCustomException(String message) {
        super(message) ;
        this.message = message ;
    }

    public String getExceptionMessage() {
        return this.message ;
    }

    @Override
    public String toString() {
        return getClass().getName() + "异常:" + this.message;
    }
}
