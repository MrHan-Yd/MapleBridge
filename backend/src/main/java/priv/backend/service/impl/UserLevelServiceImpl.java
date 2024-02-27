package priv.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import priv.backend.domain.dto.UserLevel;
import priv.backend.domain.vo.request.RestUserLevelVO;
import priv.backend.domain.vo.response.RespUserLevelVO;
import priv.backend.mapper.UserLevelMapper;
import priv.backend.service.UserLevelService;
import priv.backend.util.CurrentUtils;
import priv.backend.util.PageUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 用户等级表业务层实现类
 * @CreateDate :  2024-02-06 17:30
 */
@Service
public class UserLevelServiceImpl implements UserLevelService {
    /* TODO: Written by - Han Yongding 2024/02/06 注入用户角色表DAO */
    @Resource
    private UserLevelMapper mapper ;

    /* TODO: Written by - Han Yongding 2024/02/06 注入用户DAO */
    @Resource
    private UserServiceImpl userService ;


    /* TODO: Written by - Han Yongding 2024/02/06 分页查询用户等级表数据 */
    @Override
    public Page<RespUserLevelVO> getAllUserLevel(int pageNum, int pageSize) {
        Page<UserLevel> page = new Page<>(pageNum, pageSize);
        Page<UserLevel> userLevel = mapper.getAllUserLevel(page);

        /* TODO: Written by - Han Yongding 2024/01/23 从数据库中查询后转为响应对象返回 */
        List<RespUserLevelVO> list = userLevel
                .getRecords()
                .stream()
                .map(p -> p.asViewObject(RespUserLevelVO.class))
                .toList();

        return PageUtils.convertToPage(userLevel, list);
    }

    /* TODO: Written by - Han Yongding 2024/02/06 新增用户等级 */
    @Override
    public String insertUserLevel(RestUserLevelVO vo) {
        if (vo == null) {
            return "数据不能为空，请重试";
        }
        UserLevel userLevel = vo.asViewObject(UserLevel.class);

        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.insert(userLevel))) {
            return "添加失败，请重试";
        }
        return null;
    }

    /* TODO: Written by - Han Yongding 2024/02/06 修改用户等级表 */
    @Override
    public String updateUserLevel(RestUserLevelVO vo) {
        if (vo == null) {
            return "数据不能为空，请重试";
        }

        /* TODO: Written by - Han Yongding 2024/02/06 影响行数小于1行表示修改失败 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.updateById(vo.asViewObject(UserLevel.class)))) {
            return "修改失败，请重试";
        }
        return null;
    }

    /* TODO: Written by - Han Yongding 2024/02/06 删除用户等级 */
    @Override
    public String deleteUserLevel(String levelId) {
        if (levelId == null) {
            return "数据不能为空，请重试";
        }
        /* TODO: Written by - Han Yongding 2024/02/06 用户表中有引用 */
        if (userService.isUsingLevelId(levelId)) {
            return "此项有引用，无法删除，请删除引用项后尝试" ;
        }

        QueryWrapper<UserLevel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("level_id", levelId);
        /* TODO: Written by - Han Yongding 2024/02/06 影响行数小于1行表示删除失败 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.delete(queryWrapper))) {
            return "删除失败，请重试";
        }
        /* TODO: Written by - Han Yongding 2024/02/06 删除成功 */
        return null;
    }

    /* TODO: Written by - Han Yongding 2024/02/09 根据经验值获取对应等级ID */
    @Override
    public String getLevelIdByExperience(Integer experience) {
        return mapper.getLevelIdByExperience(experience) ;
    }
}
