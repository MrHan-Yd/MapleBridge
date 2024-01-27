package priv.backend.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import priv.backend.util.JwtUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : JWt过滤器
 * @CreateDate :  2024-01-09 22:29
 */
@Component
public class JwtAuthorizeFilter extends OncePerRequestFilter {
    @Resource
    JwtUtils jwtUtils ;

    /** TODO: Written by - Han Yongding 2023/09/17 自定义验证逻辑 */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        /* TODO: Written by - Han Yongding 2023/09/17 获取前端请求中token */
        String authorization = request.getHeader("Authorization") ;
        /* TODO: Written by - Han Yongding 2023/09/17 解析Jwt */
        DecodedJWT jwt = jwtUtils.resolveJwt(authorization) ;
        /* TODO: Written by - Han Yongding 2023/09/17 不为空则据需操作 */
        if (jwt != null) {
            /* TODO: Written by - Han Yongding 2023/09/17 解析jwt中的用户 */
            UserDetails userDetails = jwtUtils.toUser(jwt) ;
            /* TODO: Written by - Han Yongding 2023/09/17 SpringSecurity内部认证的token, 手动授权 */
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()) ;
            /* TODO: Written by - Han Yongding 2023/09/17 设置额外的认证信息,创建Web认证详情对象 */
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)) ;
            SecurityContextHolder.getContext().setAuthentication(authenticationToken) ;
            /* TODO: Written by - Han Yongding 2023/09/17 将用户id存入request中, 可以不存，存是为了方便后面写业务 */
            request.setAttribute("id", jwtUtils.toId(jwt)) ;
        }
        filterChain.doFilter(request, response) ;
    }
}
