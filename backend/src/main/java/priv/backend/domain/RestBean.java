package priv.backend.domain;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import org.apache.ibatis.io.ResolverUtil;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 响应前端请求返回对象
 * @CreateDate :  2023-09-11 16:34
 */
public record RestBean<T>(int code, T data, String message) {
    /* TODO: Written by - Han Yongding 2023/09/11 请求成功 */
    public static <T> RestBean<T> success(T data) {
        return new RestBean<T>(200, data, "请求成功") ;
    }
    public static <T> RestBean<T> success() {
        return success(null) ;
    }

    /* TODO: Written by - Han Yongding 2023/09/11 请求失败 */
    public static <T> RestBean<T> failure(int code, String message) {
        return new RestBean<T>(code, null, message) ;
    }

    /* TODO: Written by - Han Yongding 2023/09/11 未登录 */
    public static <T> RestBean<T> unauthorized(String message) {
        return failure(401, message) ;
    }

    /* TODO: Written by - Han Yongding 2023/09/11 用户登录但是没有权限 */
    public static <T> RestBean<T> forbidden(String message) {
        return failure(403, message) ;
    }

    /* TODO: Written by - Han Yongding 2023/09/11 Json格式化 */
    public String asJsonString() {
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls) ;
    }

}
