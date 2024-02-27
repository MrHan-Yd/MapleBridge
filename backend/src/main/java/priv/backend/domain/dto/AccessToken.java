package priv.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : Token
 * @CreateDate :  2024-02-17 17:19
 */
@Data
@AllArgsConstructor
public class AccessToken {
    private String accessToken ;
    private Date accessTokenExpire ;
}
