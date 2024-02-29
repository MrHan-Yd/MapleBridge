package priv.backend.service;

import priv.backend.domain.vo.request.RestFeedbackVO;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 反馈表业务层
 * @CreateDate :  2024-02-29 17:20
 */
public interface FeedbackService {

    /* TODO: Written by - Han Yongding 2024/02/29 查询反馈 */
    Object getFeedback(int pageNum, int pageSize, boolean isItPaginated) ;

    /* TODO: Written by - Han Yongding 2024/02/29 新增反馈 */
    String insertFeedback(RestFeedbackVO vo) ;

    /* TODO: Written by - Han Yongding 2024/02/29 修改反馈 */
    String updateFeedback(RestFeedbackVO vo) ;

    /* TODO: Written by - Han Yongding 2024/02/29 删除反馈 */
    String deleteFeedback(String feedbackId) ;
}
