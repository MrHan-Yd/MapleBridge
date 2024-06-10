package priv.backend.service;

import priv.backend.domain.vo.request.RestLoginConfirmVO;
import priv.backend.domain.vo.request.RestConfirmVO;
import priv.backend.domain.vo.request.RestEmailRegisterVO;
import priv.backend.domain.vo.request.RestEmailResetVO;
import priv.backend.exception.custom.ProgramCustomException;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 账户相关业务层
 * @CreateDate :  2024-02-16 20:23
 */
public interface AccountService {

    /** TODO: Written by - Han Yongding 2024/02/16 注册邮箱验证码
     * @param type 发送的邮件类型
     * @param email 需要接收的邮箱
     * @param ip 发送的ip
     * @return 结果
     * */
    String registerEmailVerifyCode(String type, String email, String ip) ;

    /** TODO: Written by - Han Yongding 2024/02/16 邮箱注册服务
     * @param vo 用于接收前端请求的实体类
     * @return 注册结果
     * */
    String registerEmailAccount(RestEmailRegisterVO vo) ;


    /** TODO: Written by - Han Yongding 2024/02/16 重置密码邮件确认
     * @param vo 前端重置密码验证码请求实体类
     * @return 结果
     *  */
    String resetConfirm(RestConfirmVO vo) ;

    /**
     * TODO: Written by - Han Yongding 2024/03/27 登录邮件确认
     *
     * @param vo 前端重置密码验证码请求实体类
     * @return 结果
     */
    String emailConfirm(RestLoginConfirmVO vo) ;

    /* TODO: Written by - Han Yongding 2024/02/17 token过期，返回新token */
    String refreshToken(String refreshToken) throws ProgramCustomException ;

    /* TODO: Written by - Han Yongding 2024/02/27 重置密码 */
    String resetPassword(RestEmailResetVO vo) ;
}
