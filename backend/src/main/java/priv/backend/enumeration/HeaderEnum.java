package priv.backend.enumeration;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 请求头或响应头常用枚举
 * @CreateDate :  2024-06-10 16:51
 */
public enum HeaderEnum {
    ACCESS_CONTROL_EXPOSE_HEADERS("Access-Control-Expose-Headers"),
    AUTHORIZATION("Authorization"),
    REFRESH_TOKEN("RefreshToken");

    public final String headerName;
    HeaderEnum(String headerName) {
        this.headerName = headerName;
    }
}
