package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import priv.backend.domain.dto.Feedback;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 反馈表DAO
 * @CreateDate :  2024-02-29 17:16
 */

public interface FeedbackMapper extends BaseMapper<Feedback> {

    /* TODO: Written by - Han Yongding 2024/02/29 分页查询反馈信息 */
    Page<Feedback> getPageFeedback(Page<Feedback> page) ;

    /* TODO: Written by - Han Yongding 2024/02/29 查询所有反馈信息 */
    List<Feedback> getAllFeedback() ;
}
