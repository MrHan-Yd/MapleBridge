package priv.backend.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import priv.backend.domain.dto.UserPreferences;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 用户偏好类业务层接口
 * @CreateDate :  2024-05-14 21:48
 */
public interface UserPreferencesService extends IService<UserPreferences> {

    /* TODO: Written by - Han Yongding 2024/05/14 插入用户偏好 */

    boolean insert(UserPreferences userPreferences);

    /* TODO: Written by - Han Yongding 2024/05/14 修改用户偏好 */

    boolean updateByPreferencesId(UserPreferences userPreferences);

    /* TODO: Written by - Han Yongding 2024/05/14 根据用户id查询用户偏好所有信息 */
    UserPreferences selectByUserId(String userId);

    /* TODO: Written by - Han Yongding 2024/07/08 根据用户id查询用户偏好 */
    UserPreferences selectPreferenceByUserId(String userId) ;

}
