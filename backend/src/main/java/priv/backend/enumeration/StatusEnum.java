package priv.backend.enumeration;

import lombok.Getter;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 状态枚举
 * @CreateDate :  2024-01-26 15:52
 */
@Getter
public enum StatusEnum {
    NORMAL("0"),
    DISABLED("1"),
    DELETE("2");


    public final String STATE;

    StatusEnum(String STATE) {
        this.STATE = STATE;
    }

}
