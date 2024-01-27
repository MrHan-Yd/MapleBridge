package priv.backend.enumeration;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 响应前端Code
 * @CreateDate :  2023-09-14 15:06
 */
public enum RestCodeEnum {
    /* TODO: Written by - Han Yongding 2023/09/14
        1、LOGIN_FAILURE：登录失败
        2、EXIT_LOGIN_FAILED：退出登录失败
    */
    LOGIN_FAILURE(401) ,
    EXIT_LOGIN_FAILED(400) ;


    private int code ;

    RestCodeEnum() {
    }

    RestCodeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
