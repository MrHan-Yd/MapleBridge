package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import priv.backend.domain.dto.UserAvatars;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 用户头像路径表DAO
 * @CreateDate :  2024-04-06 12:50
 */
public interface UserAvatarsMapper extends BaseMapper<UserAvatars> {

    /* TODO: Written by - Han Yongding 2024/04/06 根据头像ID查询对应头像数据 */
    UserAvatars getUserAvatarsByAvatarsId(String avatarsId) ;
}
