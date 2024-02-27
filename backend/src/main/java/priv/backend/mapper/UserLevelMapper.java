package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import priv.backend.domain.dto.UserLevel;
import priv.backend.domain.vo.response.RespUserLevelVO;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 用户等级表DAO
 * @CreateDate :  2024-02-06 17:28
 */
public interface UserLevelMapper extends BaseMapper<UserLevel> {
    /* TODO: Written by - Han Yongding 2024/02/06 分页查询用户等级表 */
    Page<UserLevel> getAllUserLevel(@Param("page") Page<UserLevel> page);

    /* TODO: Written by - Han Yongding 2024/02/08 根据经验值查询对应的的用户等级ID */
    String getLevelIdByExperience(Integer experience) ;

    /* TODO: Written by - Han Yongding 2024/02/12 根据等级id查询对用的等级名称 */
    RespUserLevelVO getLevelNameById(String levelId) ;
}
