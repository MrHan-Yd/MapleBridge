package priv.backend.enumeration;

import lombok.AllArgsConstructor;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 锁枚举
 * @CreateDate :  2024-05-28 21:18
 */
@AllArgsConstructor
public enum LockEnum {
    SERVER_PARAMETERS("lock:lk_server_parameters") ;

    public final String lockName;

}
