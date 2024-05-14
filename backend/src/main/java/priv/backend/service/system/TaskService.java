package priv.backend.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import priv.backend.domain.dto.Task;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 任务业务层接口
 * @CreateDate :  2024-05-11 11:05
 */
public interface TaskService extends IService<Task> {

    /* TODO: Written by - Han Yongding 2024/05/11 批量插入任务 */
    Integer batchInsertTask(List<Task> taskList) ;

    /* TODO: Written by - Han Yongding 2024/05/11 插入或更新 */

    Boolean insertOrUpdateTask(Task task) ;

    /* TODO: Written by - Han Yongding 2024/05/11 根据任务名称查询任务 */
    Task selectTaskByTaskName(String taskName) ;

    /* TODO: Written by - Han Yongding 2024/05/11 根据任务名称更新 */
    Boolean updateTaskByTaskName(Task task) ;

    /* TODO: Written by - Han Yongding 2024/05/11 根据任务名称删除 */
    Boolean deleteTaskByTaskName(String taskName) ;

    /* TODO: Written by - Han Yongding 2024/05/11 查询所有任务，用于前端计划选择(没有计划的任务) */
    List<Task> getAllTasks() ;


}
