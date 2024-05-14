package priv.backend.mapper;

import priv.backend.config.mybatis.MyBaseMapper;
import priv.backend.domain.dto.Task;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 任务DAO
 * @CreateDate :  2024-05-11 11:03
 */
public interface TaskMapper extends MyBaseMapper<Task> {

    /* TODO: Written by - Han Yongding 2024/05/11 查询所有任务，用于前端计划选择(没有计划的任务) */
    List<Task> getAllTasks() ;
}
