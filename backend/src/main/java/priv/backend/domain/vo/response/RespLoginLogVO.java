package priv.backend.domain.vo.response;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 返回前端的登录日志VO
 * @CreateDate :  2024-06-16 0:06
 */
@Data
public class RespLoginLogVO {
    private String username ;
    private LocalDateTime loginTime ;
    private String loginDevice ;
    private String loginResult ;
    private String ip ;
    private String country ;
    private String province ;
    private String region ;
    private String network ;
}
