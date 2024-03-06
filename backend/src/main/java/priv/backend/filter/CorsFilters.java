package priv.backend.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import priv.backend.util.Const;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 自定义跨域过滤器，解决跨域问题
 * @CreateDate :  2024-01-09 22:26
 */
@Component
/* TODO: Written by - Han Yongding 2023/09/17 SpringSecurity优先级，SpringSecurity优先级默认是-100，我们要比他提前处理，此处设置为-102 */
@Order(Const.ORDER_CORS)
public class CorsFilters extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain) throws IOException, ServletException {
        /* TODO: Written by - Han Yongding 2023/09/17 添加跨域请求头 */
        this.addCorsHeader(request, response) ;

        /* TODO: Written by - Han Yongding 2023/09/17 放行所有请求 */
        chain.doFilter(request, response) ;
    }

    /** TODO: Written by - Han Yongding 2023/09/17 添加跨域响应头 */
    public  void addCorsHeader(HttpServletRequest request,
                               HttpServletResponse response) {
        /* TODO: Written by - Han Yongding 2023/08/13 将Access-Control-Allow-Origin设置为浏览器请求发过来的Origin，request.getHeader("Origin")：从发起请求的源站点获取 */
        /* TODO: Written by - Han Yongding 2023/08/13 也就是说可以指定为：http://localhost:5173这个url，这么写表示获取发送请求的url，允许所有请求的意思，谁发起，我就允许谁 */
        response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin")) ;
        /* TODO: Written by - Han Yongding 2023/08/13 添加允许请求 */
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS") ;
        /* TODO: Written by - Han Yongding 2023/08/13 配置请求头 */
        response.addHeader("Access-Control-Allow-Headers", "Authorization, Content-Type") ;
    }
}
