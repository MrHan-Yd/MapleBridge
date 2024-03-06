package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import priv.backend.domain.dto.StatusUser;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 用户状态表DAO
 * @CreateDate :  2024-02-08 11:27
 */
public interface StatusUserMapper extends BaseMapper<StatusUser> {
    /** TODO: Written by - Han Yongding 2024/02/08 分页查询所有用户状态 */
    Page<StatusUser> getAllStatusUsers(Page<StatusUser> page);
}
