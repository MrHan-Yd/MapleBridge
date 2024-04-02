package priv.backend.enumeration;

import lombok.AllArgsConstructor;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 作用
 * @CreateDate :  2024-04-01 17:28
 */
@AllArgsConstructor
public enum LogTypeEnum {
    info("通知"),
    warn("警告"),
    debug("测试"),
    error("通知");

    public final String type ;

}
