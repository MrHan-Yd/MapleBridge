package priv.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import priv.backend.domain.dto.UserProfile;
import priv.backend.mapper.UserProfileMapper;
import priv.backend.service.UserProfileService;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 用户简介表业务层实现类
 * @CreateDate :  2024-04-06 12:43
 */
@Service
public class UserProfileServiceImpl extends ServiceImpl<UserProfileMapper, UserProfile> implements UserProfileService {
    /* TODO: Written by - Han Yongding 2024/04/11 注入用户见解Mapper */
    private final UserProfileMapper mapper ;

    @Autowired
    public UserProfileServiceImpl(UserProfileMapper mapper) {
        this.mapper = mapper ;
    }

    /* TODO: Written by - Han Yongding 2024/04/11 保存简介 */
    @Override
    public String saveUserProfile(UserProfile userProfile) {
        return this.saveOrUpdate(userProfile) ? userProfile.getProfileId() : null ;
    }
}
