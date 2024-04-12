package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import priv.backend.domain.dto.Profile;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 个人信息表DAO
 * @CreateDate :  2024-04-06 12:59
 */
public interface ProfileMapper extends BaseMapper<Profile> {

    /* TODO: Written by - Han Yongding 2024/04/06 根据用户唯一标识查询对应数据 */
    Profile getProFileByUserId(String userId) ;
}
