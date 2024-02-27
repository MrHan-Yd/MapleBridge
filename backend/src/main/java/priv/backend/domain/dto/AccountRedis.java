package priv.backend.domain.dto;

import lombok.Data;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 账户redis缓存
 * @CreateDate :  2024-02-17 17:49
 */
@Data
public class AccountRedis {
    private String id ;
    private String account ;
    private String password ;
    private String email ;
    private String roleId ;
    private String refreshToken ;
    private Date refreshTokenExpire ;
}
