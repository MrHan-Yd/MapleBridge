package priv.backend.service.system.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import priv.backend.domain.PageBean;
import priv.backend.domain.TaskExecutionPlan;
import priv.backend.domain.dto.TaskPlan;
import priv.backend.domain.vo.request.RestTaskPlanVO;
import priv.backend.domain.vo.response.RespTaskPlanVo;
import priv.backend.mapper.TaskPlanMapper;
import priv.backend.service.system.TaskPlanService;
import priv.backend.util.CurrentUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 任务计划表业务层实现类
 * @CreateDate :  2024-05-11 16:55
 */
@Service
public class TaskPlanServiceImpl extends ServiceImpl<TaskPlanMapper, TaskPlan> implements TaskPlanService {
    /* TODO: Written by - Han Yongding 2024/05/11 注入任务计划表Dao */
     private final TaskPlanMapper mapper ;

    @Autowired
    public TaskPlanServiceImpl(TaskPlanMapper mapper) {
        this.mapper = mapper;
    }


    /* TODO: Written by - Han Yongding 2024/05/11 插入任务计划 */
    @Override
    public String insertTaskPlan(RestTaskPlanVO vo) {
        if (vo  == null) {
            return "任务计划不能为空";
        }

        /* TODO: Written by - Han Yongding 2024/05/15 查询数据库中是否存在相同的任务计划，如果有就删除，因为数据库做了伪删除和字段唯一，不删除插入会报错 */

        if (this.deleteTaskPlanByTaskId(vo.getTaskId()) != null) {
            return "任务计划没有完全清空，且本次删除操作失败，无法继续制定计划，请联系管理员处理" ;
        } else {
            /* TODO: Written by - Han Yongding 2024/05/11 初始化 */
            TaskPlan viewObject = vo.asViewObject(TaskPlan.class);
            viewObject.setStatus("0");
            viewObject.setCreateTime(CurrentUtils.getTheCurrentSystemTime());
            /* TODO: Written by - Han Yongding 2024/05/11 插入数据库 */
            if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.insert(viewObject))) {
                return "任务计划插入失败";
            }

            /* TODO: Written by - Han Yongding 2024/05/11 业务结束返回成功信息  */
            return null;
        }
    }

    /* TODO: Written by - Han Yongding 2024/05/11 修改任务计划 */
    @Override
    public String updateTaskPlan(RestTaskPlanVO vo) {
        if (vo  == null) {
            return "任务计划不能为空";
        }
        /* TODO: Written by - Han Yongding 2024/05/11 初始化 */
        TaskPlan viewObject = vo.asViewObject(TaskPlan.class);
        viewObject.setUpdateTime(CurrentUtils.getTheCurrentSystemTime());
        /* TODO: Written by - Han Yongding 2024/05/11 修改数据库 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.updateById(viewObject))) {
            return "任务计划修改失败";
        }

        /* TODO: Written by - Han Yongding 2024/05/11 业务结束返回成功信息  */
        return null;
    }

    /* TODO: Written by - Han Yongding 2024/05/11 查询所有任务计划 */
    @Override
    public Page<RespTaskPlanVo> getAllTaskPlan(PageBean pageBean) {
        Page<TaskPlan> page = new Page<>(pageBean.getPageNum(), pageBean.getPageSize());
        return mapper.getTaskPlanPage(page);
    }

    /* TODO: Written by - Han Yongding 2024/05/15 根据任务ID删除任务计划 */

    @Override
    public String deleteTaskPlanByTaskId(String taskId) {
        if (taskId == null) {
            return "任务id不能为空";
        }

        /* TODO: Written by - Han Yongding 2024/05/15 删除任务计划，真删除 */
        QueryWrapper<TaskPlan> wrapper = new QueryWrapper<>();
        wrapper.eq("task_id", taskId);
        if (!this.remove(wrapper)) {
            return "任务计划删除失败";
        }

        /* TODO: Written by - Han Yongding 2024/05/15 删除成功 */
        return null ;
    }

    /* TODO: Written by - Han Yongding 2024/05/11 删除任务计划 */
    @Override
    public String deleteTaskPlan(String id) {
        if (id == null) {
            return "任务计划id不能为空";
        }
        /* TODO: Written by - Han Yongding 2024/05/11 删除数据(伪删除) */
        TaskPlan viewObject = new TaskPlan();

        viewObject.setId(id);
        viewObject.setStatus("2");
        /* TODO: Written by - Han Yongding 2024/05/11 修改数据库 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.updateById(viewObject))) {
            return "任务计划删除失败";
        }
        /* TODO: Written by - Han Yongding 2024/05/11 业务结束返回成功信息  */
        return null;
    }

    /* TODO: Written by - Han Yongding 2024/05/13 查询所有任务的执行计划 */
    @Override
    public List<TaskExecutionPlan> getTaskPlan() {
        return mapper.getTaskPlan() ;
    }
}
