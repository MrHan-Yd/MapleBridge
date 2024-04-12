package priv.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import priv.backend.domain.dto.Profile;
import priv.backend.mapper.ProfileMapper;
import priv.backend.service.ProfileService;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 个人信息表业务层实现类
 * @CreateDate :  2024-04-06 13:00
 */
@Service
public class ProfileServiceImpl extends ServiceImpl<ProfileMapper, Profile> implements ProfileService {

    /* TODO: Written by - Han Yongding 2024/04/11 注入个人信息表Mapper */
    private final ProfileMapper mapper ;

    @Autowired
    public ProfileServiceImpl(ProfileMapper mapper) {
        this.mapper = mapper ;
    }

    /* TODO: Written by - Han Yongding 2024/04/11 保存个人信息 */
    @Override
    public String saveProfile(Profile profile) {
        return this.saveOrUpdate(profile) ? profile.getProfileId() : null ;
    }
}
