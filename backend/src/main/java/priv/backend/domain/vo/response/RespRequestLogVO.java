package priv.backend.domain.vo.response;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 响应前端请求日志VO
 * @CreateDate :  2024-06-16 16:19
 */
@Data
public class RespRequestLogVO {
    private String url ;
    private String method ;
    private String ip ;
    private String position ;
    private String parameter ;
    private String responseValue ;
    private String timeConsuming ;
    private LocalDateTime requestTime ;
    private String country ;
    private String province ;
    private String region ;
    private String network ;
    private String deviceType ;
    private String osName ;
    private String browserType ;
    private String browserVersion ;
}
