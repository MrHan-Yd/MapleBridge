package priv.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.IService;
import priv.backend.domain.dto.User;
import priv.backend.domain.dto.UserAvatars;
import priv.backend.mapper.UserAvatarsMapper;
import priv.backend.mapper.UserMapper;
import priv.backend.service.UserAvatarsService;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 用户头像路径表业务层实现类
 * @CreateDate :  2024-04-06 12:51
 */
@Service
public class UserAvatarsServiceImpl extends ServiceImpl<UserAvatarsMapper, UserAvatars> implements UserAvatarsService {
    /* TODO: Written by - Han Yongding 2024/04/11 注入用户头像Mapper */
    private final UserAvatarsMapper mapper ;

    @Autowired
    public UserAvatarsServiceImpl(UserAvatarsMapper mapper) {
        this.mapper = mapper ;
    }

    /* TODO: Written by - Han Yongding 2024/04/11 保存头像数据 */
    public String saveUserAvatars(UserAvatars userAvatars) {
        return this.saveOrUpdate(userAvatars) ? userAvatars.getAvatarsId() : null ;
    }
}
