package priv.backend.controller;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import priv.backend.domain.RestBean;
import priv.backend.domain.vo.request.RestConfirmVO;
import priv.backend.domain.vo.request.RestEmailRegisterVO;
import priv.backend.domain.vo.request.RestEmailResetVO;
import priv.backend.enumeration.CodeEnum;
import priv.backend.enumeration.RestCodeEnum;
import priv.backend.exception.custom.ProgramCustomException;
import priv.backend.service.impl.AccountServiceImpl;
import priv.backend.util.JwtUtils;
import priv.backend.util.ReturnUtils;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 处理验证相关的控制器
 * @CreateDate :  2024-01-22 16:00
 */
@RestController
@RequestMapping("/api/auth/")
public class AuthorizeController {

    /* TODO: Written by - Han Yongding 2024/02/16 注入账户相关业务层 */
    @Resource
    private AccountServiceImpl accountService ;

    /* TODO: Written by - Han Yongding 2024/02/16 获取邮箱验证码 */
    @GetMapping("ask-code")
    public RestBean<Void> askVerifyCode(@RequestParam("email") @Email String email,
                                        @RequestParam("type") @Pattern(regexp = "register|reset") String type,
                                        HttpServletRequest request) {
        return ReturnUtils.messageHandle(() -> accountService.registerEmailVerifyCode(type, email, request.getRemoteAddr())) ;
    }

    /** TODO: Written by - Han Yongding 2024/02/16 注册 */
    @PostMapping("register")
    public RestBean<Void> register(@RequestBody RestEmailRegisterVO vo) {
        return ReturnUtils.messageHandle(vo, accountService::registerEmailAccount) ;
    }

    /** TODO: Written by - Han Yongding 2024/02/16 重置密码邮箱确认 */
    @GetMapping("reset-confirm")
    public RestBean<Void> resetConfirm(@Valid RestConfirmVO vo) {
        return ReturnUtils.messageHandle(vo, accountService::resetConfirm) ;
    }

    /* TODO: Written by - Han Yongding 2024/02/27 重置密码 */
    @PutMapping("reset-password")
    public RestBean<Void> resetPassword(@RequestBody @Valid RestEmailResetVO vo) {
        return ReturnUtils.messageHandle(vo, accountService::resetPassword) ;
    }

    /** TODO: Written by - Han Yongding 2023/10/27 token过期，提供给前端刷新 */
    @GetMapping("refresh-token/{refreshToken}")
    public RestBean<Object> refreshToken(@PathVariable("refreshToken") @Valid String refreshToken) {
        try {
            return RestBean.success(accountService.refreshToken(refreshToken)) ;
        } catch (ProgramCustomException e) {
            return RestBean.failure(CodeEnum.HTTP_400_ERROR_REQUEST.CODE, e.getExceptionMessage()) ;
        }
    }
}
