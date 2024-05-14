package priv.backend.service.system.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import priv.backend.domain.dto.Task;
import priv.backend.mapper.TaskMapper;
import priv.backend.service.system.TaskService;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 任务业务层实现类
 * @CreateDate :  2024-05-11 11:05
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {
    /* TODO: Written by - Han Yongding 2024/05/11 注入任务DAO层 */
    private final TaskMapper mapper ;

    @Autowired
    public TaskServiceImpl(TaskMapper mapper) {
        this.mapper = mapper;
    }

    /* TODO: Written by - Han Yongding 2024/05/11 批量插入任务 */
    @Override
    public Integer batchInsertTask(List<Task> taskList) {
        return mapper.insertBatchSomeColumn(taskList) ;
    }


    /* TODO: Written by - Han Yongding 2024/05/11 插入或更新 */

    @Override
    public Boolean insertOrUpdateTask(Task task) {
        return this.saveOrUpdate(task) ;
    }

    /* TODO: Written by - Han Yongding 2024/05/11 根据任务名称查询任务 */
    @Override
    public Task selectTaskByTaskName(String taskName) {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_name", taskName);
        return this.getOne(queryWrapper) ;
    }

    /* TODO: Written by - Han Yongding 2024/05/11 根据任务名称更新 */
    @Override
    public Boolean updateTaskByTaskName(Task task) {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>() ;
        queryWrapper.eq("task_name", task.getTaskName()) ;
        return this.update(task, queryWrapper) ;
    }
    /* TODO: Written by - Han Yongding 2024/05/11 根据任务名称删除 */
    @Override
    public Boolean deleteTaskByTaskName(String taskName) {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>() ;
        queryWrapper.eq("task_name", taskName) ;
        return this.remove(queryWrapper) ;
    }

    /* TODO: Written by - Han Yongding 2024/05/11 查询所有任务，用于前端计划选择 */
    @Override
    public List<Task> getAllTasks() {
        return mapper.getAllTasks() ;
    }
}
