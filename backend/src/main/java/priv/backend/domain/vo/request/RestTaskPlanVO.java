package priv.backend.domain.vo.request;

import lombok.Data;
import priv.backend.domain.BaseData;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 接收前端任务计划的请求参数
 * @CreateDate :  2024-05-11 18:06
 */
@Data
public class RestTaskPlanVO  implements BaseData {
    private String id ;
    private String taskId ;
    private String cron ;
    private String status ;
    private String remarks ;
    private String createId ;
    private String updateId ;
}
