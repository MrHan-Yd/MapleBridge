package priv.backend.domain.vo.response;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 响应前端反馈请求实体
 * @CreateDate :  2024-02-29 17:42
 */
@Data
public class RespFeedbackVO {
    private String feedbackId ;
    private String userId ;
    private String feedbackText ;
    private Timestamp timestamp ;
}
