package priv.backend.filter;

import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import priv.backend.domain.RestBean;
import priv.backend.util.Const;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 限流过滤器，阻止用户过于频繁请求
 * @CreateDate :  2024-01-09 22:27
 */
@Component
@Order(Const.ORDER_LIMIT)
public class FlowLimitFilter extends HttpFilter {
    @Resource
    StringRedisTemplate template ;
    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain) throws IOException, ServletException {
        /* TODO: Written by - Han Yongding 2023/09/17 获取用户IP */
        String remoteAddr = request.getRemoteAddr() ;
        /* TODO: Written by - Han Yongding 2023/09/17 判断用户是否被封禁 */
        if (this.tryCount(remoteAddr)) {
            /* TODO: Written by - Han Yongding 2023/09/17 没有封禁则放行 */
            chain.doFilter(request, response) ;
        } else {
            /* TODO: Written by - Han Yongding 2023/09/17 告知用户，请求已拦截 */
            this.writeBlockMessage(response) ;
        }

    }

    /** TODO: Written by - Han Yongding 2023/09/17 判断用户是否被封禁 */
    private boolean tryCount(String ip) {
        /* TODO: Written by - Han Yongding 2023/09/17 为防止同时请求，同时进行判断，同时通过，所以对ip地址加锁 */
        synchronized (ip.intern()) {
            /* TODO: Written by - Han Yongding 2023/09/17 判断用户是否被封禁 */

            if (Boolean.TRUE.equals(template.hasKey(Const.FLOW_LIMIT_BLOCK + ip))) {
                return false ;
            }
            /* TODO: Written by - Han Yongding 2023/09/17 请求频率限制 */
            return this.limitPeriodCheck(ip) ;
        }
    }

    /** TODO: Written by - Han Yongding 2023/09/17 请求频率限制 */
    private boolean limitPeriodCheck(String ip) {
        /* TODO: Written by - Han Yongding 2023/09/17 redis中已有 */
        if (Boolean.TRUE.equals(template.hasKey(Const.FLOW_LIMIT_COUNTER + ip))) {
            /* TODO: Written by - Han Yongding 2023/09/17 计数自增,有可能会刚过期，最小值为0 */
            long increment = Optional.ofNullable(template.opsForValue().increment(Const.FLOW_LIMIT_COUNTER + ip)).orElse(0L) ;

            /* TODO: Written by - Han Yongding 2023/09/17 如果三秒请求大于二十次，表示已经被封禁 */
            if(increment > 20) {
                /* TODO: Written by - Han Yongding 2023/09/17 封禁30秒 */
                template.opsForValue().set(Const.FLOW_LIMIT_BLOCK + ip, "", 30, TimeUnit.SECONDS) ;
                return false ;
            }
        } else {
            /* TODO: Written by - Han Yongding 2023/09/17 如果redis中没有，创建一个，以特殊字段+ip为key，值为1，只存在3秒 */
            template.opsForValue().set(Const.FLOW_LIMIT_COUNTER + ip, "1", 3, TimeUnit.SECONDS) ;
        }
        return true ;
    }

    /** TODO: Written by - Han Yongding 2023/09/17 告知用户，请求已被拦截 */
    private void writeBlockMessage(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN) ;
        response.setContentType("application/json") ;
        response.setCharacterEncoding("UTF-8") ;
        response.getWriter().write(RestBean.forbidden("操作频繁，请稍后再试").asJsonString()) ;
    }
}
