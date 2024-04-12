package priv.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;
import priv.backend.domain.Files;
import priv.backend.domain.dto.Profile;
import priv.backend.domain.dto.User;
import priv.backend.domain.dto.UserAvatars;
import priv.backend.domain.dto.UserProfile;
import priv.backend.domain.es.dto.ESClientUser;
import priv.backend.domain.vo.request.RestClientUserVO;
import priv.backend.domain.vo.request.RestUserVO;
import priv.backend.domain.vo.response.RespUserVO;
import priv.backend.enumeration.DataBaseEnum;
import priv.backend.mapper.UserLevelMapper;
import priv.backend.mapper.UserMapper;
import priv.backend.repository.ESClientUserRepository;
import priv.backend.service.UserService;
import priv.backend.util.CurrentUtils;
import priv.backend.util.LogUtils;
import priv.backend.util.RandomStringUtil;
import priv.backend.util.UploadUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private PasswordEncoder passwordEncoder;

    /* TODO: Written by - Han Yongding 2024/02/16 注入角色业务层 */
    @Resource
    private RoleServiceImpl roleService;

    /* TODO: Written by - Han Yongding 2024/03/28 注入RabbitMQ模板 */
    @Resource
    private AmqpTemplate amqpTemplate;

    /* TODO: Written by - Han Yongding 2024/04/07 注入ES用户数据DAO */
    @Resource
    private ESClientUserRepository esClientUserRepository;

    /* TODO: Written by - Han Yongding 2024/04/11 注入上传头像工具类 */
    @Resource
    private UploadUtils uploadUtils;

    /**
     * TODO: Written by - Han Yongding 2024/02/06 根据等级ID查询用户等级是否被使用
     */
    @Override
    public boolean isUsingLevelId(String levelId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("level_id", levelId);
        long count = mapper.selectCount(queryWrapper);
        return count > 0;
    }

    /**
     * TODO: Written by - Han Yongding 2024/02/08 新增用户
     */

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
                v.setPassword(DataBaseEnum.INITIAL_PASSWORD.contents);
            }
            /* TODO: Written by - Han Yongding 2024/02/14 加密 */
            String password = passwordEncoder.encode(v.getPassword());
            v.setPassword(password);
            v.setExperience(0);
            v.setRegisterTime(CurrentUtils.getTheCurrentSystemTime());
            /* TODO: Written by - Han Yongding 2024/02/08 在用 */
            v.setStatusId("1755492769986392066");
            /* TODO: Written by - Han Yongding 2024/03/27 后端管理员新增会选择角色，前端注册无法携带角色，默认普通用户 */
            if (v.getRoleId() == null) {
                v.setRoleId("1758482033179316225");
            }
            /* TODO: Written by - Han Yongding 2024/04/01 前台注册,无法选择用户, 上面会赋值为普通用户 */
            if ("1758482033179316225".equals(v.getRoleId())) {
                /* TODO: Written by - Han Yongding 2024/04/01 初始化昵称 */
                v.setNickname("用户" + RandomStringUtil.generateRandomNumericString(4));
            }
            /* TODO: Written by - Han Yongding 2024/04/01 后台新增, 选择了管理员用户 */
            if ("1752529962403770370".equals(v.getRoleId())) {
                /* TODO: Written by - Han Yongding 2024/04/01 初始化昵称 */
                v.setNickname("管理" + RandomStringUtil.generateRandomNumericString(4));
            }
            /* TODO: Written by - Han Yongding 2024/03/01 获取当前系统时间 */
            v.setCreateTime(CurrentUtils.getTheCurrentSystemTime());
        });

        /* TODO: Written by - Han Yongding 2024/02/16 插入失败 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.insert(user))) {
            return "新增失败，请稍后重试";
        }

        /* TODO: Written by - Han Yongding 2024/03/28 注册成功，发送邮件通知 */
        Map<String, Object> data = Map.of("type", "registerSuccess", "email", user.getEmail());

        amqpTemplate.convertAndSend("notice", data);

        /* TODO: Written by - Han Yongding 2024/03/28 插入创建人 */
        if (user.getCreateId() == null) {
            user.setCreateId(user.getId());
            mapper.updateById(user);
        }

        /* TODO: Written by - Han Yongding 2024/02/08 新增成功 */
        return null;
    }

    /**
     * TODO: Written by - Han Yongding 2024/02/11 修改用户信息
     */
    @Override
    public String updateUser(RestUserVO vo) {
        if (vo == null) {
            return "数据为空，请填写后重试";
        }

        User user = vo.asViewObject(User.class);

        /* TODO: Written by - Han Yongding 2024/02/15 密码不为空则加密密码 */
        if (!CurrentUtils.isEmpty(vo.getPassword())) {
            /* TODO: Written by - Han Yongding 2024/02/15 加密 */
            String password = passwordEncoder.encode(vo.getPassword());
            user.setPassword(password);
        }

        /* TODO: Written by - Han Yongding 2024/03/28 修改时间记录 */
        user.setUpdateTime(CurrentUtils.getTheCurrentSystemTime());

        /* TODO: Written by - Han Yongding 2024/02/12 修改失败 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.updateById(user))) {
            return "修改失败，请稍后重试";
        }

        /* TODO: Written by - Han Yongding 2024/02/12 修改成功 */
        return null;
    }

    /**
     * TODO: Written by - Han Yongding 2024/02/12 分页查询所有用户
     */
    @Override
    public Page<RespUserVO> getAllUser(int pageNum, int pageSize) {
        Page<RespUserVO> page = new Page<>(pageNum, pageSize);
        /* TODO: Written by - Han Yongding 2024/02/12 查询所有用户 */
        return mapper.getAllUser(page);
    }


    /**
     * TODO: Written by - Han Yongding 2024/02/13 根据ID删除用户， 伪删除
     */
    @Override
    public String deleteUserById(String id) {
        if (id == null) {
            return "唯一表示为空，请填写后重试";
        }

        /* TODO: Written by - Han Yongding 2024/03/28 伪删除 */
        User user = new User();
        user.setId(id);
        user.setStatusId("1755493103987208193");
        /* TODO: Written by - Han Yongding 2024/02/12 删除失败 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.updateById(user))) {
            return "删除失败，请稍后重试";
        }

        /* TODO: Written by - Han Yongding 2024/02/12 删除成功 */
        return null;
    }

    /**
     * TODO: Written by - Han Yongding 2024/02/16 根据用户名或邮箱查询账号
     */
    public User findAccountByNameOrEmail(String nameOrEmail) {
        return this.query()
                .eq("account", nameOrEmail)
                .or()
                .eq("email", nameOrEmail)
                .one();
    }

    /**
     * TODO: Written by - Han Yongding 2024/02/27 重写Security登录验证
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User account = this.findAccountByNameOrEmail(username);
        if (account == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        /* TODO: Written by - Han Yongding 2024/02/16 获取对应角色Name */
        String roleName = roleService.getRoleById(account.getRoleId()).getRoleName();
        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(account.getPassword())
                .roles(roleName)
                .build();
    }

    /* TODO: Written by - Han Yongding 2024/04/07 查询所有用户信息，同步es使用 */
    @Override
    public List<ESClientUser> getClientUserES() {
        return mapper.getClientUserES();
    }

    /* TODO: Written by - Han Yongding 2024/04/07 根据用户ID，查询对应用户信息 */
    @Override
    public Optional<ESClientUser> getClientUserById(String userId) {
        return esClientUserRepository.findById(userId);
    }

    /* TODO: Written by - Han Yongding 2024/04/11 客户端修改用户信息事务 */
    @Resource
    private TransactionTemplate updateClientUserTemplate;

    /* TODO: Written by - Han Yongding 2024/04/11 注入用户头像业务层实现类 */
    @Resource
    private UserAvatarsServiceImpl avatarsService;

    /* TODO: Written by - Han Yongding 2024/04/11 注入用户简介业务层实现类 */
    @Resource
    private UserProfileServiceImpl userProfileService;

    /* TODO: Written by - Han Yongding 2024/04/11 注入用户个人信息表业务层实现类 */
    @Resource
    private ProfileServiceImpl profileService ;


    /* TODO: Written by - Han Yongding 2024/04/11 客户端修改用户信息 */
    @Override
    public String updateClientUserById(RestClientUserVO vo) {
        return updateClientUserTemplate.execute(status -> {
            System.out.println(vo);
            if (vo == null) {
                return "数据为空，请按照要求填写数据";
            }
            /* TODO: Written by - Han Yongding 2024/04/11 写出头像,头像唯一标识符 */
            String avatarsId = null;
            if (vo.getAvatars() != null && vo.getAvatars().getSize() != 0) {
                /* TODO: Written by - Han Yongding 2024/04/11 头像唯一标识 */
                avatarsId = this.saveUserAvatars(vo.getId(), vo.getAvatars());

                if (avatarsId == null) {
                    /* TODO: Written by - Han Yongding 2024/04/11 回滚数据 */
                    status.setRollbackOnly();
                    return "保存失败";
                }
                /* TODO: Written by - Han Yongding 2024/04/11 保存成功 */
            }

            /* TODO: Written by - Han Yongding 2024/04/11 插入个人简介 */
            /* TODO: Written by - Han Yongding 2024/04/11 简介唯一标识符，有就修改，没有就插入生成 */
            String userProfileId = this.saveUserProfile(vo.getProfileId(), vo.getBio());
            /* TODO: Written by - Han Yongding 2024/04/11 个人简介保存失败 */
            if (userProfileId == null) {
                /* TODO: Written by - Han Yongding 2024/04/11 保存失败，删除头像并回滚数据 */
                this.deleteUserAvatars(vo.getId());
                status.setRollbackOnly();
                return "保存失败";
            }

            /* TODO: Written by - Han Yongding 2024/04/11 插入个人信息 */
            String profileId = this.saveProfile(vo.getId(), vo.getLastName(), vo.getFirstName(),
                    vo.getRegion(), vo.getLocation(), vo.getGraduationDepartment(),
                    vo.getMajor(), vo.getGraduationYear()) ;
            /* TODO: Written by - Han Yongding 2024/04/11 个人信息保存失败 */
            if (profileId == null) {
                /* TODO: Written by - Han Yongding 2024/04/11 保存失败，删除头像并回滚数据 */
                this.deleteUserAvatars(vo.getId());
                status.setRollbackOnly();
                return "保存失败" ;
            }

            /* TODO: Written by - Han Yongding 2024/04/11 插入更新表 */
            Boolean rs = this.saveUser(vo.getId(), vo.getNickname(), vo.getGender(), vo.getBirthday(), avatarsId, userProfileId);
            if (!rs) {
                /* TODO: Written by - Han Yongding 2024/04/11 删除写出文件 */
                this.deleteUserAvatars(vo.getId());
                /* TODO: Written by - Han Yongding 2024/04/11 回滚前面插入的操作 */
                status.setRollbackOnly();
                return "保存失败" ;
            }
            /* TODO: Written by - Han Yongding 2024/04/11 保存成功，同步es */
            amqpTemplate.convertAndSend("clientUserSyncES", vo.getId()) ;
            /* TODO: Written by - Han Yongding 2024/04/11 业务结束 */
            return null ;
        });
    }

    /* TODO: Written by - Han Yongding 2024/04/11 保存头像 */
    private String saveUserAvatars(String userId, MultipartFile mf) {
        return updateClientUserTemplate.execute(status -> {
            /* 用户上传头像 */
            String path = uploadUtils.generateSavePath(userId, "avatars");
            UserAvatars userAvatars = new UserAvatars();
            userAvatars.setUserId(userId);
            userAvatars.setFileSize(String.valueOf(mf.getSize() / 1024.0));
            Files files;
            try {
                files = uploadUtils.saveFile(mf, path);
                userAvatars.setAvatarPath("avatars");
                userAvatars.setFileName(files.getFileName() + files.getFileSuffix());
                userAvatars.setUpdateTime(files.getFileTimestamp());
            } catch (IOException e) {
                String message = "头像写出失败，请稍候重试";
                LogUtils.warning(this.getClass(), message);
                return null;
            } catch (NullPointerException e) {
                String message = "头像为空，初始化失败";
                LogUtils.warning(this.getClass(), message);
                return null;
            }

            /* TODO: Written by - Han Yongding 2024/04/11 保存 */
            String avatarsRS = avatarsService.saveUserAvatars(userAvatars);
            if (avatarsRS == null) {
                /* TODO: Written by - Han Yongding 2024/04/11 删除写出的数据 */
                this.deleteUserAvatars(userId);
                return null;
            }
            /* TODO: Written by - Han Yongding 2024/04/11 保存成功 */
            return avatarsRS;
        });

    }

    /* TODO: Written by - Han Yongding 2024/04/11 插入失败，删除写出的头像文件 */
    private void deleteUserAvatars(String userId) {
        boolean avatars = uploadUtils.deleteFile(uploadUtils.generateDeletePath(userId, "avatars"));
        if (avatars) {
            LogUtils.info(this.getClass(), "删除写出文件成功");
        } else {
            LogUtils.info(this.getClass(), "删除写出文件失败，请管理员手动清除，路径:" + userId + "/" + "avatars");
        }
    }

    /* TODO: Written by - Han Yongding 2024/04/11 保存个人简介 */
    private String saveUserProfile(String profileId, String content) {
        return updateClientUserTemplate.execute(status -> userProfileService.saveUserProfile(new UserProfile(profileId, content, CurrentUtils.getTheCurrentSystemTime())));
    }

    /* TODO: Written by - Han Yongding 2024/04/11 保存个人信息 */
    private String saveProfile(String id,
                               String lastName,
                               String firstName,
                               String region,
                               String location,
                               String graduationDepartment,
                               String major,
                               String graduationYear) {
        return updateClientUserTemplate.execute(
                status -> profileService.saveProfile(new Profile(null,
                    id, lastName, firstName, region, location,
                    graduationDepartment, major, graduationYear, CurrentUtils.getTheCurrentSystemTime()))) ;
    }

    /* TODO: Written by - Han Yongding 2024/04/11 保存用户信息 */
    private Boolean saveUser(String id,
                            String nickname,
                            String gender,
                            Date birthday,
                            String avatarsId,
                            String profileId) {
        return updateClientUserTemplate.execute(status ->
                this.saveOrUpdate(new User(id, nickname, gender, birthday, avatarsId, profileId, id, CurrentUtils.getTheCurrentSystemTime()))) ;
    }

    /* TODO: Written by - Han Yongding 2024/04/11 根据用户ID查询用户信息，同步ES使用 */
    @Override
    public ESClientUser getClientUserByUserId(String userId) {
        return mapper.getClientUserByUserId(userId) ;
    }
}
