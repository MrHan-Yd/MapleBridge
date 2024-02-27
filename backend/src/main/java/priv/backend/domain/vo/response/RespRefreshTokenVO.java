package priv.backend.domain.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 响应前端刷新token请求实体类
 * @CreateDate :  2023-11-02 8:44
 */
@Data
@AllArgsConstructor
public class RespRefreshTokenVO {

    private String token ;
    private Date tokenExpire ;

}
