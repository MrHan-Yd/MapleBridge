package priv.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import priv.backend.domain.dto.Profile;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 个人信息表业务层
 * @CreateDate :  2024-04-06 13:00
 */
public interface ProfileService extends IService<Profile> {
    /* TODO: Written by - Han Yongding 2024/04/11 保存个人信息 */
    String saveProfile(Profile profile) ;
}
