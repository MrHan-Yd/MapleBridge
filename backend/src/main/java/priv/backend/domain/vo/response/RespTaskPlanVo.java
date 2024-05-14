package priv.backend.domain.vo.response;

import lombok.Data;
import priv.backend.domain.dto.Task;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 响应任务计划
 * @CreateDate :  2024-05-11 20:48
 */
@Data
public class RespTaskPlanVo {
    private String id ;
    private Task task ;
    private String cron ;
    private String status ;
    private String remarks ;
    private String createId ;
    private Date createTime ;
    private String updateId ;
    private Date updateTime ;
}
