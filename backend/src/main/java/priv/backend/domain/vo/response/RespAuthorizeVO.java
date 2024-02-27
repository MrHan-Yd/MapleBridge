package priv.backend.domain.vo.response;

import lombok.Data;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 登录成功响应前端的实体
 * @CreateDate :  2024-02-16 14:37
 */
@Data
public class RespAuthorizeVO {
    private String account ;
    private RespUserRoleNoIdVO role ;
    private String accessToken ;
    private Date accessTokenExpire ;
    private String refreshToken ;
    private Date refreshTokenExpire ;
}
