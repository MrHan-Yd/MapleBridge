package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import priv.backend.domain.dto.UserProfile;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 用户简介表DAO
 * @CreateDate :  2024-04-06 12:42
 */
public interface UserProfileMapper extends BaseMapper<UserProfile> {

    /* TODO: Written by - Han Yongding 2024/04/06 根据ProfileId获取对应的用户简介 */
    UserProfile getUserProfileByProfileId(String profileId) ;
}
