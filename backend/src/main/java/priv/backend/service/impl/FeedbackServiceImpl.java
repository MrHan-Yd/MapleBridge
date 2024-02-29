package priv.backend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import priv.backend.domain.dto.Feedback;
import priv.backend.domain.dto.TypesWorkExperience;
import priv.backend.domain.vo.request.RestFeedbackVO;
import priv.backend.domain.vo.response.RespFeedbackVO;
import priv.backend.domain.vo.response.RespTypesWorkExperienceVO;
import priv.backend.mapper.FeedbackMapper;
import priv.backend.service.FeedbackService;
import priv.backend.util.CurrentUtils;
import priv.backend.util.PageUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 反馈表业务层实现类
 * @CreateDate :  2024-02-29 17:21
 */
@Service
public class FeedbackServiceImpl implements FeedbackService {

    /* TODO: Written by - Han Yongding 2024/02/29 注入反馈表DAO */
    @Resource
    private FeedbackMapper mapper ;


    /* TODO: Written by - Han Yongding 2024/02/29 查询反馈 */
    @Override
    public Object getFeedback(int pageNum, int pageSize, boolean isItPaginated) {
        /* TODO: Written by - Han Yongding 2024/02/28 判断是否需要分页 */
        if (isItPaginated) {
            Page<Feedback> page = new Page<>(pageNum, pageSize);
            Page<Feedback> returnPage = mapper.getPageFeedback(page);

            /* TODO: Written by - Han Yongding 2024/02/28 拷贝属性 */
            List<RespFeedbackVO> list = returnPage
                    .getRecords()
                    .stream()
                    .map(p -> p.asViewObject(RespFeedbackVO.class))
                    .toList();

            return PageUtils.convertToPage(returnPage, list);
        }
        /* TODO: Written by - Han Yongding 2024/02/28 不分页直接返回 */
        return mapper.getAllFeedback() ;
    }

    /* TODO: Written by - Han Yongding 2024/02/29 新增反馈 */
    @Override
    public String insertFeedback(RestFeedbackVO vo) {

        /* TODO: Written by - Han Yongding 2024/02/29 数据为空，返回 */
        if (vo == null) {
            return "数据不能为空，请稍后重试";
        }

        /* TODO: Written by - Han Yongding 2024/02/28 拷贝对象 */
        Feedback feedback = vo.asViewObject(Feedback.class) ;
        /* TODO: Written by - Han Yongding 2024/02/28 获取系统当前时间 */
        feedback.setTimestamp(CurrentUtils.getTheCurrentSystemTime()); ;

        /* TODO: Written by - Han Yongding 2024/02/28 新增 */
        if(CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.insert(feedback))) {
            return "新增失败，请稍后重试";
        }

        /* TODO: Written by - Han Yongding 2024/02/28 新增成功 */
        return null;
    }

    /* TODO: Written by - Han Yongding 2024/02/29 修改反馈 */
    @Override
    public String updateFeedback(RestFeedbackVO vo) {
        /* TODO: Written by - Han Yongding 2024/02/29 数据为空，返回 */
        if (vo == null) {
            return "数据不能为空，请稍后重试";
        }

        /* TODO: Written by - Han Yongding 2024/02/28 根据id修改 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.updateById(vo.asViewObject(Feedback.class)))) {
            return "修改失败，请稍后重试";
        }

        /* TODO: Written by - Han Yongding 2024/02/28 修改成功 */
        return null;
    }

    /* TODO: Written by - Han Yongding 2024/02/29 删除反馈 */
    @Override
    public String deleteFeedback(String feedbackId) {
        Feedback vo = new Feedback() ;
        vo.setFeedbackId(feedbackId);

        /* TODO: Written by - Han Yongding 2024/02/29 删除对应数据 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.deleteById(vo))) {
            return "删除失败，请稍候再试" ;
        }

        /* TODO: Written by - Han Yongding 2024/02/28 删除成功 */
        return null;
    }
}
