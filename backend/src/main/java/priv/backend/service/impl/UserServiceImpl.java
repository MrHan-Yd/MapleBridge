package priv.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import priv.backend.domain.dto.User;
import priv.backend.domain.vo.request.RestUserVO;
import priv.backend.domain.vo.response.RespUserVO;
import priv.backend.mapper.UserLevelMapper;
import priv.backend.mapper.UserMapper;
import priv.backend.service.UserService;
import priv.backend.util.CurrentUtils;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 用户业务层实现类
 * @CreateDate :  2024-02-06 22:55
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    /* TODO: Written by - Han Yongding 2024/02/06 注入用户表DAO */
    @Resource
    private UserMapper mapper;

    /* TODO: Written by - Han Yongding 2024/02/09 注入用户等级表DAO, 因为和业务层循环依赖 */
    @Resource
    private UserLevelMapper userLevelMapper;

    /* TODO: Written by - Han Yongding 2024/02/14 注入密码加密工具 */
    @Resource
    private PasswordEncoder passwordEncoder ;

    /* TODO: Written by - Han Yongding 2024/02/16 注入角色业务层 */
    @Resource
    private RoleServiceImpl roleService ;

    /** TODO: Written by - Han Yongding 2024/02/06 根据等级ID查询用户等级是否被使用 */
    @Override
    public boolean isUsingLevelId(String levelId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("level_id", levelId);
        long count = mapper.selectCount(queryWrapper);
        return count > 0;
    }

    /** TODO: Written by - Han Yongding 2024/02/08 新增用户 */

    @Override
    public String insertUser(RestUserVO vo) {
        if (vo == null) {
            return "数据为空，请填写后重试";
        }

        /* TODO: Written by - Han Yongding 2024/02/11 复制属性到用户实体类 */
        User user = vo.asViewObject(User.class, v -> {
            /* TODO: Written by - Han Yongding 2024/02/08 初始化用户信息 */
            /* TODO: Written by - Han Yongding 2024/02/09 初始化等级经验值为0 */
            v.setLevelId(userLevelMapper.getLevelIdByExperience(0));
            /* TODO: Written by - Han Yongding 2024/02/11 密码为空初始化为xyh123456 */
            if (CurrentUtils.isEmpty(v.getPassword())) {
                v.setPassword("xyh123456");
            }
            /* TODO: Written by - Han Yongding 2024/02/14 加密 */
            String password = passwordEncoder.encode(v.getPassword()) ;
            v.setPassword(password) ;
            v.setExperience(0);
            v.setRegisterTime(CurrentUtils.getTheCurrentSystemTime());
            /* TODO: Written by - Han Yongding 2024/02/08 在用 */
            v.setStatusId("1755492769986392066");
            /* TODO: Written by - Han Yongding 2024/03/01 获取当前系统时间 */
            v.setCreateTime(CurrentUtils.getTheCurrentSystemTime());
        });

        /* TODO: Written by - Han Yongding 2024/02/16 插入失败 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.insert(user))) {
            return "新增失败，请稍后重试";
        }

        /* TODO: Written by - Han Yongding 2024/02/08 新增成功 */
        return null;
    }

    /** TODO: Written by - Han Yongding 2024/02/11 修改用户信息 */
    @Override
    public String updateUser(RestUserVO vo) {
        if (vo == null) {
            return "数据为空，请填写后重试";
        } ;

        User user = vo.asViewObject(User.class) ;

        /* TODO: Written by - Han Yongding 2024/02/15 密码不为空则加密密码 */
        if (!CurrentUtils.isEmpty(vo.getPassword())) {
            /* TODO: Written by - Han Yongding 2024/02/15 加密 */
            String password = passwordEncoder.encode(vo.getPassword()) ;
            user.setPassword(password) ;
        }

        /* TODO: Written by - Han Yongding 2024/02/12 修改失败 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.updateById(user))) {
            return "修改失败，请稍后重试";
        }

        /* TODO: Written by - Han Yongding 2024/02/12 修改成功 */
        return null;
    }

    /** TODO: Written by - Han Yongding 2024/02/12 分页查询所有用户 */
    @Override
    public Page<RespUserVO> getAllUser(int pageNum, int pageSize) {
        Page<RespUserVO> page = new Page<>(pageNum, pageSize);
        /* TODO: Written by - Han Yongding 2024/02/12 查询所有用户 */
        return mapper.getAllUser(page);
    }


    /** TODO: Written by - Han Yongding 2024/02/13 根据ID删除用户， 伪删除 */
    @Override
    public String deleteUserById(String id) {
        if (id == null) {
            return "唯一表示为空，请填写后重试";
        } ;

        User user = new User() ;
        user.setId(id) ;
        user.setStatusId("1755493103987208193") ;
        /* TODO: Written by - Han Yongding 2024/02/12 删除失败 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.updateById(user))) {
            return "删除失败，请稍后重试";
        }

        /* TODO: Written by - Han Yongding 2024/02/12 删除成功 */
        return null;
    }

    /** TODO: Written by - Han Yongding 2024/02/16 根据用户名或邮箱查询账号 */
    public User findAccountByNameOrEmail(String nameOrEmail) {
        return this.query()
                .eq("account", nameOrEmail)
                .or()
                .eq("email", nameOrEmail)
                .one() ;
    }

    /** TODO: Written by - Han Yongding 2024/02/27 重写Security登录验证 */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User account = this.findAccountByNameOrEmail(username) ;
        if (account == null) {
            throw new UsernameNotFoundException("用户名或密码错误") ;
        }
        /* TODO: Written by - Han Yongding 2024/02/16 获取对应角色Name */
        String roleName = roleService.getRoleById(account.getRoleId()).getRoleName() ;
        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(account.getPassword())
                .roles(roleName)
                .build();
    }
}
