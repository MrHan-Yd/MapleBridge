package priv.backend.domain.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 响应前端刷新令牌请求，颁发新令牌使用的实体类
 * @CreateDate :  2023-11-02 10:55
 */
@Data
@AllArgsConstructor
public class RespCreateJwtVO {
    private String id ;
    private String username ;
    private List<String> authorities ;
}
