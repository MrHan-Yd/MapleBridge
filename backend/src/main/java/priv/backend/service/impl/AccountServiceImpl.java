package priv.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import priv.backend.domain.dto.User;
import priv.backend.domain.vo.request.*;
import priv.backend.domain.vo.response.RespRefreshTokenVO;
import priv.backend.exception.custom.ProgramCustomException;
import priv.backend.mapper.UserMapper;
import priv.backend.service.AccountService;
import priv.backend.util.*;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 账户相关业务层实现类
 * @CreateDate :  2024-02-16 20:23
 */
@Slf4j
@Service
public class AccountServiceImpl extends ServiceImpl<UserMapper, User> implements AccountService {

    /* TODO: Written by - Han Yongding 2024/03/26 注入用户Mapper */
    @Resource
    private UserMapper mapper ;

    /* TODO: Written by - Han Yongding 2024/02/16 注入用户业务层实现类 */
    @Resource
    private UserServiceImpl userService;

    /* TODO: Written by - Han Yongding 2024/03/04 注入限流工具类 */
    @Resource
    private FlowUtils flowUtils;

    /* TODO: Written by - Han Yongding 2024/03/04 注入RabbitMQ消息队列 */
    @Resource
    AmqpTemplate amqpTemplate;

    /* TODO: Written by - Han Yongding 2024/03/04 注入redisString */
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /* TODO: Written by - Han Yongding 2024/02/14 注入密码加密工具 */
    @Resource
    private PasswordEncoder passwordEncoder;

    /* TODO: Written by - Han Yongding 2024/02/17 注入Jwt工具类  */
    @Resource
    private JwtUtils jwtUtils;


    /**
     * TODO: Written by - Han Yongding 2024/02/16 注册邮箱验证码
     *
     * @param type  发送的邮件类型
     * @param email 需要接收的邮箱
     * @param ip    发送的ip
     * @return 结果
     */
    @Override
    public String registerEmailVerifyCode(String type, String email, String ip) {
        /* TODO: Written by - Han Yongding 2023/09/18 加锁，防止同一时间被多次调用(会排队), 用intern做锁是因为可以提升性能 */
        synchronized (ip.intern()) {
            /* TODO: Written by - Han Yongding 2023/09/18 如果没通过验证 */
            if (!this.verifyLimit(ip)) {
                return "请求频繁，请稍后再试!";
            }

            /* TODO: Written by - Han Yongding 2024/03/26  */
            String emailKey = email ;

            /* TODO: Written by - Han Yongding 2024/03/28 怕redis缓存中有该用户的验证码，所以先清空，确保一个邮箱只有一条验证码 */
            stringRedisTemplate.delete(Const.VERIFY_EMAIL_DATA + emailKey) ;

            /* TODO: Written by - Han Yongding 2024/03/26 判断用户是邮箱还是账号，用户登录可能使用的是账号，需要从数据库中获取用户的邮箱 */
            if (!email.contains("@")) {
                String dataEmail = mapper.getEmailByAccount(email) ;
                if (CurrentUtils.isEmpty(dataEmail)) {
                    return "账号有误" ;
                }
                email = dataEmail ;
            }


            /* TODO: Written by - Han Yongding 2024/01/10 获取随机的六位数字字符串 */
            String code = RandomStringUtils.getRandomSixDigitCode();

            /* TODO: Written by - Han Yongding 2023/09/18 存储创建好的随机验证码 */
            Map<String, Object> data = Map.of("type", type, "email", email, "code", code);

            /* TODO: Written by - Han Yongding 2023/09/18 将验证码放入注册好的消息队列中 */
            amqpTemplate.convertAndSend("mail", data);

            /* TODO: Written by - Han Yongding 2023/09/18 用于校验使用，将验证码放入redis中, 过期时间3分钟 */
            stringRedisTemplate.opsForValue()
                    .set(Const.VERIFY_EMAIL_DATA + emailKey, code, 3, TimeUnit.MINUTES);
            /* TODO: Written by - Han Yongding 2023/09/18 redis简单的限流 */
            return null;
        }
    }

    /**
     * TODO: Written by - Han Yongding 2024/02/16 限流前检查
     */
    private boolean verifyLimit(String ip) {
        String key = Const.VERIFY_EMAIL_LIMIT + ip;
        /* TODO: Written by - Han Yongding 2023/09/18 限制60秒 */
        return flowUtils.limitOnceCheck(key, 60);
    }


    /**
     * TODO: Written by - Han Yongding 2024/02/16 邮箱注册服务
     *
     * @param vo 用于接收前端请求的实体类
     * @return 注册结果
     */
    @Override
    public String registerEmailAccount(RestEmailRegisterVO vo) {
        /* TODO: Written by - Han Yongding 2023/09/20 获取邮箱和用户名 */
        String account = vo.getAccount();
        String email = vo.getEmail();

        /* TODO: Written by - Han Yongding 2023/09/20 验证码在redis中的key */
        String key = Const.VERIFY_EMAIL_DATA + email;

        /* TODO: Written by - Han Yongding 2023/09/20 获取redis中缓存的验证码 */
        String code = stringRedisTemplate.opsForValue().get(key);

        /* TODO: Written by - Han Yongding 2023/09/20 如果用户没有申请验证码 */
        if (code == null) {
            return "请先获取验证码";
        }

        /* TODO: Written by - Han Yongding 2023/09/20 如果缓存的验证码和用户填入的验证码不相等 */
        if (!code.equals(vo.getCode())) {
            return "验证码输入错误，请重新输入";
        }

        /* TODO: Written by - Han Yongding 2023/09/20 如果邮箱已经注册过 */
        if (this.existsAccountByEmail(email)) {
            return "此电子邮件已被其他用户注册";
        }
        /* TODO: Written by - Han Yongding 2023/09/20 如果用户名存在 */
        if (this.existsAccountByUserName(account)) {
            return "用户名已被其他用户注册，请更换其他用户名";
        }

        /* TODO: Written by - Han Yongding 2023/09/20 封装好要注册的对象 */
        RestUserVO insertVO = new RestUserVO();
        insertVO.setAccount(account);
        insertVO.setEmail(email);


        /* TODO: Written by - Han Yongding 2023/09/20 保存到数据库 */
        String message = userService.insertUser(insertVO);
        if (message != null) {
            /* TODO: Written by - Han Yongding 2024/02/16 注册失败，返回失败原因 */
            return message;
        } else {
            /* TODO: Written by - Han Yongding 2024/02/16 注册成功，删除注册验证码 */
            stringRedisTemplate.delete(key);
            return null;
        }
    }

    /**
     * TODO: Written by - Han Yongding 2024/02/16 通过邮箱判断是否存在，存在则表明已经注册过了
     *
     * @param email 邮箱
     * @return 非null
     */
    public boolean existsAccountByEmail(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>() ;
        queryWrapper.eq("email", email) ;
        return userService.getOne(queryWrapper) != null ;
    }

    /**
     * TODO: Written by - Han Yongding 2024/02/16 通过用户判断是否存在，存在则表明已经注册过了
     *
     * @param account 用户名
     * @return 非null
     */
    public boolean existsAccountByUserName(String account) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>() ;
        queryWrapper.eq("account", account) ;
        return userService.getOne(queryWrapper) != null ;
    }


    /**
     * TODO: Written by - Han Yongding 2024/02/16 重置密码邮件确认
     *
     * @param vo 前端重置密码验证码请求实体类
     * @return 结果
     */
    @Override
    public String resetConfirm(RestConfirmVO vo) {
        String email = vo.getEmail();
        String code = stringRedisTemplate.opsForValue().get(Const.VERIFY_EMAIL_DATA + email);

        /* TODO: Written by - Han Yongding 2023/09/23 没有获取验证码 */
        if (code == null) {
            return "请先获取验证码";
        }

        /* TODO: Written by - Han Yongding 2023/09/23 验证码不正确 */
        if (!code.equals(vo.getCode())) {
            return "验证码错误，请重新输入";
        }

        return null;
    }

    /**
     * TODO: Written by - Han Yongding 2024/03/27 登录邮件确认
     *
     * @param vo 前端重置密码验证码请求实体类
     * @return 结果
     */
    @Override
    public String emailConfirm(RestLoginConfirmVO vo) {
        String key = Const.VERIFY_EMAIL_DATA + vo.getAccountOrEmail() ;
        /* TODO: Written by - Han Yongding 2024/03/27 缓存的验证码 */
        String code = stringRedisTemplate.opsForValue().get(key) ;

        /* TODO: Written by - Han Yongding 2024/03/27 没有获取验证码 */
        if (code == null) {
            return "请先获取验证码";
        }

        /* TODO: Written by - Han Yongding 2023/09/23 验证码不正确 */
        if (!code.equals(vo.getCode())) {
            return "验证码错误，请重新输入";
        }

        /* TODO: Written by - Han Yongding 2024/03/30 清除缓存验证码 */
        stringRedisTemplate.delete(key) ;
        /* TODO: Written by - Han Yongding 2024/03/27 验证成功 */
        return null;
    }

    /** TODO: Written by - Han Yongding 2024/02/17 token过期，返回新token */
    @Override
    public String refreshToken(String refreshToken) throws ProgramCustomException {
        /* TODO: Written by - Han Yongding 2024/02/17 令牌不能为空 */
        if (refreshToken == null) {
            throw new ProgramCustomException("刷新令牌为空，无法颁发新令牌") ;
        }
        /* TODO: Written by - Han Yongding 2024/03/03 截取刷新Token */
        String refreshTokens = jwtUtils.convertToken(refreshToken) ;

        /* TODO: Written by - Han Yongding 2023/11/02 为截取到令牌，表示令牌不合法 */
        if (refreshTokens == null) {
            throw new ProgramCustomException("刷新令牌不存在或不合法");
        }


        /* TODO: Written by - Han Yongding 2023/11/09 jwt工具类的tokenExpiredOrNot方法解析
         * tokenExpiredOrNot方法是用当前时间去判断是否超过了规定时间
         * 此方法中第一次使用是判断令牌是否已过期，所以需要取反，此方法最初定义是为了判断令牌没有过期
         * 此方法中第二次使用是判断令牌没有过期，所以直接使用，不需要取反
         * */
        /* TODO: Written by - Han Yongding 2023/11/09 刷新token已过期，请重新登录 */
        if (!jwtUtils.refreshTokenExpiredOrNot(refreshTokens)) {
            throw new ProgramCustomException("刷新令牌已过期");
        }

        log.info("正在为用户颁发新令牌");
        /* TODO: Written by - Han Yongding 2023/10/27 颁发新令牌 */
        return jwtUtils.issueANewTokenByRefreshToken(refreshTokens) ;
    }

    /**
     * 重置密码
     * @param vo 用户邮箱密码实体类
     * @return 重置结果
     */
    @Override
    public String resetPassword(RestEmailResetVO vo) {
        String email = vo.getEmail() ;
        /* TODO: Written by - Han Yongding 2024/02/27 重置密码验证，将电子邮件和验证码传入，需要转换成 ConfirmResetVO对象 */
        String verify = this.resetConfirm(new RestConfirmVO(vo.getEmail(), vo.getCode())) ;

        /* TODO: Written by - Han Yongding 2024/02/27 验证不通过 */
        if (verify != null) {
            return verify ;
        }

        /* TODO: Written by - Han Yongding 2024/02/27 验证码正确，重置密码 */
        String password = passwordEncoder.encode(vo.getPassword()) ;
        /* TODO: Written by - Han Yongding 2024/02/27 根据邮箱修改密码 */
        boolean update = this.update().eq("email", email).set("password", password).update() ;
        /* TODO: Written by - Han Yongding 2023/08/18 更新成功 */
        if(update) {
            stringRedisTemplate.delete(Const.VERIFY_EMAIL_DATA + email) ;
        } else {
            return "密码重置失败，请稍后再试!" ;
        }
        /* TODO: Written by - Han Yongding 2024/02/27 修改成功 */
        return null;
    }
}
