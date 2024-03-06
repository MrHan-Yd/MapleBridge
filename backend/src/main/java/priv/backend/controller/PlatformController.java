package priv.backend.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import priv.backend.domain.RestBean;
import priv.backend.domain.vo.request.RestAnnouncementVO;
import priv.backend.domain.vo.request.RestStatusAnnouncementVO;
import priv.backend.domain.vo.request.RestFeedbackVO;
import priv.backend.domain.vo.request.RestTypesAnnouncementVO;
import priv.backend.service.impl.AnnouncementServiceImpl;
import priv.backend.service.impl.FeedbackServiceImpl;
import priv.backend.service.impl.StatusAnnouncementServiceImpl;
import priv.backend.service.impl.TypesAnnouncementServiceImpl;
import priv.backend.util.ReturnUtils;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 平台功能控制器
 * @CreateDate :  2024-03-04 12:34
 */
@RestController
@RequestMapping("/api/auth/")
public class PlatformController {
    /** TODO: Written by - Han Yongding 2024/02/29 注入用户反馈业务层 */
    @Resource
    private FeedbackServiceImpl feedbackService;

    /** TODO: Written by - Han Yongding 2024/02/29 查询所有用户反馈 */
    @GetMapping("feedback")
    public RestBean<Object> getAllFeedback(@RequestParam(defaultValue = "1") int pageNum,
                                           @RequestParam(defaultValue = "10") int pageSize,
                                           @RequestParam(required = false, defaultValue = "true") boolean isItPaginated) {
        return ReturnUtils
                .messageHandleData(() -> feedbackService.getFeedback(pageNum, pageSize, isItPaginated));
    }

    /** TODO: Written by - Han Yongding 2024/02/29 新增用户反馈 */
    @PostMapping("feedback")
    public RestBean<Void> insertFeedback(@RequestBody RestFeedbackVO vo) {
        return ReturnUtils
                .messageHandle(vo, feedbackService::insertFeedback);
    }

    /** TODO: Written by - Han Yongding 2024/02/29 修改用户反馈 */
    @PutMapping("feedback")
    public RestBean<Void> updateFeedback(@RequestBody RestFeedbackVO vo) {
        return ReturnUtils
                .messageHandle(vo, feedbackService::updateFeedback);
    }

    /** TODO: Written by - Han Yongding 2024/02/29 删除用户反馈 */
    @DeleteMapping("feedback/{feedbackId}")
    public RestBean<Void> deleteFeedback(@PathVariable("feedbackId") String feedbackId) {
        return ReturnUtils
                .messageHandle(() -> feedbackService.deleteFeedback(feedbackId));
    }

    /* TODO: Written by - Han Yongding 2024/03/04 注入公告管理业务层 */
    @Resource
    private StatusAnnouncementServiceImpl statusAnnouncementService;

    /** TODO: Written by - Han Yongding 2024/03/04 分页查询所有公告状态 */
    @GetMapping("announcement-status")
    public RestBean<Object> getPageStatusAnnouncement(@RequestParam(defaultValue = "1") int pageNum,
                                                @RequestParam(defaultValue = "10") int pageSize) {
        return ReturnUtils.messageHandleData(() -> statusAnnouncementService.getPageStatusAnnouncement(pageNum, pageSize)) ;
    }

    /* TODO: Written by - Han Yongding 2024/03/04 新增公告状态 */
    @PostMapping("announcement-status")
    public RestBean<Void> insertStatusAnnouncement(@RequestBody RestStatusAnnouncementVO vo) {
        return ReturnUtils.messageHandle(vo, statusAnnouncementService::insertAnnouncement);
    }

    /** TODO: Written by - Han Yongding 2024/03/04 修改公告状态 */
    @PutMapping("announcement-status")
    public RestBean<Void> updateStatusAnnouncement(@RequestBody RestStatusAnnouncementVO vo) {
        return ReturnUtils.messageHandle(vo, statusAnnouncementService::updateStatusAnnouncementByStatusId);
    }

    /* TODO: Written by - Han Yongding 2024/03/04 删除公告状态 */
    @DeleteMapping("announcement-status/{statusId}")
    public RestBean<Void> deleteStatusAnnouncement(@PathVariable("statusId") String statusId) {
        return ReturnUtils.messageHandle(() -> statusAnnouncementService.deleteStatusAnnouncementByStatusId(statusId));
    }

    /* TODO: Written by - Han Yongding 2024/03/04 注入公告类型业务层 */
    @Resource
    private TypesAnnouncementServiceImpl typesAnnouncementService ;

    /** TODO: Written by - Han Yongding 2024/03/04 查询所有公告类型 */
    @GetMapping("announcement-types")
    public RestBean<Object> getTypesAnnouncement(@RequestParam(defaultValue = "1") int pageNum,
                                                 @RequestParam(defaultValue = "10") int pageSize,
                                                 @RequestParam(required = false, defaultValue = "true") boolean isItPaginated) {
        return ReturnUtils.messageHandleData(() -> typesAnnouncementService.getAnnouncementTypes(pageNum, pageSize, isItPaginated));
    }

    /** TODO: Written by - Han Yongding 2024/03/04 新增公告类型 */
    @PostMapping("announcement-types")
    public RestBean<Void> insertTypesAnnouncement(@RequestBody RestTypesAnnouncementVO vo) {
        return ReturnUtils.messageHandle(vo, typesAnnouncementService::insertTypesAnnouncement);
    }

    /** TODO: Written by - Han Yongding 2024/03/04 修改公告类型 */
    @PutMapping("announcement-types")
    public RestBean<Void> updateTypesAnnouncement(@RequestBody RestTypesAnnouncementVO vo) {
        return ReturnUtils.messageHandle(vo, typesAnnouncementService::updateTypesAnnouncementByTypeId);
    }

    /** TODO: Written by - Han Yongding 2024/03/04 删除公告类型 */
    @DeleteMapping("announcement-types/{typeId}")
    public RestBean<Void> deleteTypesAnnouncement(@PathVariable("typeId") String typeId) {
        return ReturnUtils.messageHandle(() -> typesAnnouncementService.deleteTypesAnnouncementByTypeId(typeId)) ;
    }

    /* TODO: Written by - Han Yongding 2024/03/05 注入公告业务层 */
    @Resource
    private AnnouncementServiceImpl announcementService ;

    /** TODO: Written by - Han Yongding 2024/03/06 查询公告 */
    @GetMapping("announcement")
    public RestBean<Object> getAnnouncement(@RequestParam(defaultValue = "1") int pageNum,
                                   @RequestParam(defaultValue = "10") int pageSize) {
        return ReturnUtils.messageHandleData(() -> announcementService.getPageAnnouncement(pageNum, pageSize)) ;
    }

    /** TODO: Written by - Han Yongding 2024/03/05 新增公告 */
    @PostMapping("announcement")
    public RestBean<Void> insertAnnouncement(@RequestBody RestAnnouncementVO vo) {
        return ReturnUtils.messageHandle(vo, announcementService::insertAnnouncement) ;
    }

    /** TODO: Written by - Han Yongding 2024/03/06 修改公告 */
    @PutMapping("announcement")
    public RestBean<Void> updateAnnouncementById(@RequestBody RestAnnouncementVO vo) {
        return ReturnUtils.messageHandle(vo, announcementService::updateAnnouncementById) ;
    }

    /* TODO: Written by - Han Yongding 2024/03/06 删除公告 */
    @DeleteMapping("announcement/{id}")
    public RestBean<Void> deleteAnnouncementById(@PathVariable("id") String id) {
        return ReturnUtils.messageHandle(() -> announcementService.deleteAnnouncementById(id)) ;
    }
}
