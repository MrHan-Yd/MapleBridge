package priv.backend.service.system.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import priv.backend.domain.dto.UserPreferences;
import priv.backend.mapper.UserPreferencesMapper;
import priv.backend.service.system.UserPreferencesService;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 用户偏好类业务层接口实现类
 * @CreateDate :  2024-05-14 21:49
 */
@Service
public class UserPreferencesServiceImpl extends ServiceImpl<UserPreferencesMapper, UserPreferences> implements UserPreferencesService {

    /* TODO: Written by - Han Yongding 2024/05/14 插入或修改用户偏好 */
    @Override
    public boolean insert(UserPreferences userPreferences) {
        return this.save(userPreferences) ;
    }

    /* TODO: Written by - Han Yongding 2024/05/14 修改用户偏好 */
    @Override
    public boolean updateByPreferencesId(UserPreferences userPreferences) {
        return this.updateById(userPreferences);
    }

    /* TODO: Written by - Han Yongding 2024/05/14 根据用户id查询用户偏好 */
    @Override
    public UserPreferences selectByUserId(String userId){
        return this.query().eq("user_id", userId).one();
    }
}
