package priv.backend.service.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import priv.backend.domain.PageBean;
import priv.backend.domain.TaskExecutionPlan;
import priv.backend.domain.dto.TaskPlan;
import priv.backend.domain.vo.request.RestTaskPlanVO;
import priv.backend.domain.vo.response.RespTaskPlanVo;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 任务计划表业务层
 * @CreateDate :  2024-05-11 16:55
 */
public interface TaskPlanService extends IService<TaskPlan> {

    /* TODO: Written by - Han Yongding 2024/05/11 插入任务计划 */
    String insertTaskPlan(RestTaskPlanVO vo) ;

    /* TODO: Written by - Han Yongding 2024/05/11 修改任务计划 */
    String updateTaskPlan(RestTaskPlanVO vo) ;

    /* TODO: Written by - Han Yongding 2024/05/11 查询所有任务计划 */
    Page<RespTaskPlanVo> getAllTaskPlan(PageBean pageBean) ;

    /* TODO: Written by - Han Yongding 2024/05/11 删除任务计划 */
    String deleteTaskPlan(String id) ;

    /* TODO: Written by - Han Yongding 2024/05/13 查询所有任务的执行计划 */
    List<TaskExecutionPlan> getTaskPlan() ;

    /* TODO: Written by - Han Yongding 2024/05/15 根据任务ID删除任务计划 */
    String deleteTaskPlanByTaskId(String taskId) ;
}
