package priv.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import priv.backend.domain.vo.request.RestUserLevelVO;
import priv.backend.domain.vo.response.RespUserLevelVO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 用户等级表业务层
 * @CreateDate :  2024-02-06 17:29
 */
public interface UserLevelService {

    /* TODO: Written by - Han Yongding 2024/02/06 查询所有等级表数据 */
    Page<RespUserLevelVO> getAllUserLevel(int pageNum, int pageSize) ;
    /* TODO: Written by - Han Yongding 2024/02/06 查询所有等级表数据 */
    List<RespUserLevelVO> getAllUserLevel() ;

    /* TODO: Written by - Han Yongding 2024/02/06 新增用户等级 */
    String insertUserLevel(RestUserLevelVO vo) ;

    /* TODO: Written by - Han Yongding 2024/02/06 修改用户等级表 */
    String updateUserLevel(RestUserLevelVO vo) ;

    /* TODO: Written by - Han Yongding 2024/02/06 删除用户等级 */
    String deleteUserLevel(String levelId) ;

    /* TODO: Written by - Han Yongding 2024/02/09 根据经验值获取对应等级ID */
    String getLevelIdByExperience(Integer experience) ;
}
