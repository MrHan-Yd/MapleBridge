package priv.backend.service;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 用户业务层
 * @CreateDate :  2024-02-06 22:54
 */
public interface UserService {
    /* TODO: Written by - Han Yongding 2024/02/06 根据等级id查询是否正在使用 */
    boolean isUsingLevelId(String levelId);
}
