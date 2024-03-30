package priv.backend.enumeration;

import lombok.Getter;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 存储到数据库的常用串
 * @CreateDate :  2023-09-14 15:06
 */
@Getter
public enum DataBaseEnum {
    /** TODO: 2022/7/7 存储数据库常用字符串 */
    /* TODO: 2022/7/7 未绑定邮箱, ^ 为占位符 */
    UnboundE_EMail("User:^-UnboundE-Mail"),
    INITIAL_PASSWORD("xyh123456");

    /**
     * -- GETTER --
     * TODO: 2022/7/7 get方法开始
     */
    public String contents ;

    /** TODO: 2022/7/7 构造方法开始 */
    DataBaseEnum() {
    }
    DataBaseEnum(String contents) {
        this.contents = contents;
    }
    /* TODO: 2022/7/7 构造方法结束 */

    /* TODO: 2022/7/7 get方法结束 */

}
