package priv.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import priv.backend.domain.dto.UserProfile;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 用户简介表业务层
 * @CreateDate :  2024-04-06 12:42
 */
public interface UserProfileService extends IService<UserProfile> {
    /* TODO: Written by - Han Yongding 2024/04/11 保存简介 */
    String saveUserProfile(UserProfile userProfile) ;
}
