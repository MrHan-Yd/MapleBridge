package priv.backend.domain.vo.request;

import lombok.Data;
import priv.backend.domain.BaseData;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 接收前端请求的反馈实体类
 * @CreateDate :  2024-02-29 17:36
 */
@Data
public class RestFeedbackVO implements BaseData {
    private String feedbackId ;
    private String userId ;
    private String feedbackText ;
}
