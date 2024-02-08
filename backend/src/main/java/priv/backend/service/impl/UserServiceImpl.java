package priv.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import priv.backend.domain.dto.User;
import priv.backend.mapper.UserMapper;
import priv.backend.service.UserService;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 用户业务层实现类
 * @CreateDate :  2024-02-06 22:55
 */
@Service
public class UserServiceImpl implements UserService {
    /* TODO: Written by - Han Yongding 2024/02/06 注入用户表DAO */
    @Resource
    private UserMapper mapper ;

    /* TODO: Written by - Han Yongding 2024/02/06 根据等级ID查询用户等级是否被使用 */
    @Override
    public boolean isUsingLevelId(String levelId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>() ;
        queryWrapper.eq("level_id", levelId) ;
        long count = mapper.selectCount(queryWrapper) ;
        return count > 0 ;
    }
}
