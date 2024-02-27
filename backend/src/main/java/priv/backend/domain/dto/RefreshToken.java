package priv.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 刷新Token
 * @CreateDate :  2024-02-17 17:16
 */
@Data
@AllArgsConstructor
public class RefreshToken {
    private String refreshToken ;
    private Date refreshTokenExpire ;
}
