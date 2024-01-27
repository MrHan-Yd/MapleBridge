package priv.backend.config;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import priv.backend.domain.RestBean;
import priv.backend.enumeration.RestCodeEnum;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import priv.backend.filter.JwtAuthorizeFilter;
import priv.backend.util.JwtUtils;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : Security配置类
 * @CreateDate :  2024-01-09 22:16
 */
@Configuration
public class SecurityConfig {

    /** TODO: Written by - Han Yongding 2023/10/09 注入用户实体业务层 */


    /** TODO: Written by - Han Yongding 2023/09/14 Jwt工具类 */
    @Resource
    private JwtUtils jwtUtils ;

    /** TODO: Written by - Han Yongding 2023/10/09 注入自定义拦截器 */
    @Resource
    private JwtAuthorizeFilter jwtAuthorizeFilter ;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(conf -> conf
                        .requestMatchers("/api/auth/*", "/error").permitAll() // 允许访问 "/api/auth/*" 和 "/error" 路径的请求
                        .requestMatchers("/api/auth/backend-admin/*").hasAuthority("ROLE_SUPER_ADMIN") // 需要 "role_admin" 权限才能访问 "/api/auth/backend-admin" 路径
                        .anyRequest().authenticated() // 其他请求需要进行身份验证
                )
                .formLogin(conf -> conf
                        .loginProcessingUrl("/api/auth/login") // 指定登录请求的 URL
                        .successHandler(this::onAuthenticationSuccess) // 登录成功后的处理逻辑
                        .failureHandler(this::onAuthenticationFailure) // 登录失败后的处理逻辑
                )
                .logout(conf -> conf
                        .logoutUrl("/api/auth/logout") // 指定登出请求的 URL
                        .logoutSuccessHandler(this::onLogoutSuccess) // 登出成功后的处理逻辑
                )
                .exceptionHandling(conf -> conf
                        .authenticationEntryPoint(this::onUnauthorized)
                        .accessDeniedHandler(this::onAccessDeny)
                )
                .csrf(AbstractHttpConfigurer::disable) /* 禁用 CSRF 保护 */
                .sessionManagement(conf -> conf
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 使用无状态的会话管理策略,这行代码配置了无状态的会话管理策略，不保存用户会话状态。
                )
                .addFilterBefore(jwtAuthorizeFilter, UsernamePasswordAuthenticationFilter.class)
                .build() ;
    }

    /** TODO: Written by - Han Yongding 2023/09/05 登录 */
    private void onAuthenticationSuccess(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Authentication authentication) throws IOException {
        response.setContentType("application/json") ;
        response.setCharacterEncoding("UTF-8") ;
        /* TODO: Written by - Han Yongding 2023/09/06 获取登录信息 */
        User user = (User)authentication.getPrincipal() ;
        /* TODO: Written by - Han Yongding 2023/08/11 从数据库中查询用户名，用户可能登陆的是邮箱，所以这里不能直接使用user */
//        Account account = service.findAccountByNameOrEmail(user.getUsername()) ;
//        /* TODO: Written by - Han Yongding 2023/09/11 返回Jwt(给用户颁发token) */
//        String tokenArr = jwtUtils.createTokenAndRefreshToken(user, account.getId(), account.getAccountName()) ;
//        /* TODO: Written by - Han Yongding 2023/09/11 响应前端的用户实体类 */
//        AuthorizeVO vo = account.asViewObject(AuthorizeVO.class, v -> {
//            v.setToken(jwtUtils.getToken(tokenArr)) ;
//            v.setExpire(jwtUtils.expireTime()) ;
//            v.setRefreshToken(jwtUtils.getRefreshToken(tokenArr)) ;
//        }) ;

        response.getWriter().write(RestBean.success().asJsonString()) ;
    }

    /** TODO: Written by - Han Yongding 2023/09/05 登录失败 */
    private void onAuthenticationFailure(HttpServletRequest request,
                                         HttpServletResponse response,
                                         AuthenticationException exception) throws IOException {
        response.setContentType("application/json") ;
        response.setCharacterEncoding("UTF-8") ;
        /* TODO: Written by - Han Yongding 2023/09/14 登录失败 */
        response.getWriter().write(RestBean.failure(RestCodeEnum.LOGIN_FAILURE.getCode(), exception.getMessage()).asJsonString()) ;
    }

    /** TODO: Written by - Han Yongding 2023/09/05 退出登录 */
    private void onLogoutSuccess(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Authentication authentication) throws IOException {
        response.setContentType("application/json") ;
        response.setCharacterEncoding("UTF-8") ;
        PrintWriter writer = response.getWriter() ;
        String authorization = request.getHeader("Authorization") ;
        /* TODO: Written by - Han Yongding 2023/09/14 退出登录成功 */
        if (jwtUtils.invalidateJwt(authorization)) {
            writer.write(RestBean.success().asJsonString()) ;
        } else {
            /* TODO: Written by - Han Yongding 2023/09/14 退出登录失败 */
            writer.write(RestBean.failure(RestCodeEnum.EXIT_LOGIN_FAILED.getCode(), "退出登录失败").asJsonString()) ;
        }
    }

    /** TODO: Written by - Han Yongding 2023/09/16 未登录访问需要授权的接口 */
    public void onUnauthorized(HttpServletRequest request,
                               HttpServletResponse response,
                               AuthenticationException exception) throws IOException {
        response.setContentType("application/json") ;
        response.setCharacterEncoding("UTF-8") ;
        response.getWriter().write(RestBean.unauthorized(exception.getMessage()).asJsonString()) ;
    }

    /** TODO: Written by - Han Yongding 2023/09/16 登录了，但是用户没有权限方法重写 */
    public void onAccessDeny(HttpServletRequest request,
                             HttpServletResponse response,
                             AccessDeniedException exception) throws IOException {
        response.setContentType("application/json") ;
        response.setCharacterEncoding("UTF-8") ;
        response.getWriter().write(RestBean.forbidden(exception.getMessage()).asJsonString()) ;
    }
}
