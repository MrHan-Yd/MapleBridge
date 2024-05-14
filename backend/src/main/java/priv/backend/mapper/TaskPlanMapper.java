package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import priv.backend.domain.TaskExecutionPlan;
import priv.backend.domain.dto.TaskPlan;
import priv.backend.domain.vo.response.RespTaskPlanVo;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 任务计划表Mapper
 * @CreateDate :  2024-05-11 16:54
 */
public interface TaskPlanMapper extends BaseMapper<TaskPlan> {

    /** TODO: Written by - Han Yongding 2024/01/31 分页查询任务计划 */
    Page<RespTaskPlanVo> getTaskPlanPage(@Param("page") Page<TaskPlan> page);

    /* TODO: Written by - Han Yongding 2024/05/13 查询所有任务的执行计划 */
    List<TaskExecutionPlan> getTaskPlan() ;
}
