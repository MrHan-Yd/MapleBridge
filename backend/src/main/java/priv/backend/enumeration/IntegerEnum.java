package priv.backend.enumeration;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 整型枚举
 * @CreateDate :  2024-04-15 9:12
 */
public enum IntegerEnum {
    COUNT(1) ;

    public final Integer value ;

    IntegerEnum(Integer value) {
        this.value = value;
    }
}
