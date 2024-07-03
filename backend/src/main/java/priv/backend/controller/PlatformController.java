package priv.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;
import priv.backend.domain.PageBean;
import priv.backend.domain.RestBean;
import priv.backend.domain.dto.Log;
import priv.backend.domain.vo.request.*;
import priv.backend.domain.vo.response.RespServerParametersVO;
import priv.backend.service.impl.*;
import priv.backend.service.system.Impl.ServerParametersServiceImpl;
import priv.backend.service.system.Impl.TaskPlanServiceImpl;
import priv.backend.service.system.Impl.TaskServiceImpl;
import priv.backend.util.ReturnUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 平台功能控制器
 * @CreateDate :  2024-03-04 12:34
 */
@RestController
@RequestMapping("/api/backend/")
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


    /* TODO: Written by - Han Yongding 2024/03/08 注入帖子业务层 */
    @Resource
    private TypesPostServiceImpl postService ;

    /** TODO: Written by - Han Yongding 2024/03/08 查询所有帖子类型 */
    @GetMapping("post-types")
    public RestBean<Object> getTypesPost(PageBean pageBean) {
        return ReturnUtils.messageHandleData(() -> postService.getPostTypes(pageBean));
    }

    /** TODO: Written by - Han Yongding 2024/03/08 新增帖子类型 */
    @PostMapping("post-types")
    public RestBean<Void> insertTypesPost(@RequestBody RestTypesPostVO vo) {
        return ReturnUtils.messageHandle(vo, postService::insertTypesPost);
    }

    /** TODO: Written by - Han Yongding 2024/03/08 修改帖子类型 */
    @PutMapping("post-types")
    public RestBean<Void> updateTypesPost(@RequestBody RestTypesPostVO vo) {
        return ReturnUtils.messageHandle(vo, postService::updateTypesPostByTypeId);
    }

    /** TODO: Written by - Han Yongding 2024/03/08 删除帖子类型 */
    @DeleteMapping("post-types/{typeId}")
    public RestBean<Void> deleteTypesPost(@PathVariable("typeId") String typeId) {
        return ReturnUtils.messageHandle(() -> postService.deleteTypesPostByTypeId(typeId)) ;
    }

    /* TODO: Written by - Han Yongding 2024/03/09 注入帖子业务层 */
    @Resource
    private PostServiceImpl postServiceImpl;

    /** TODO: Written by - Han Yongding 2024/03/09 分页查询帖子 */
    @GetMapping("post")
    public RestBean<Object> getPost(PageBean pageBean) {
        return ReturnUtils.messageHandleData(() -> postServiceImpl.getPagePost(pageBean)) ;
    }

    /** TODO: Written by - Han Yongding 2024/03/10 新增帖子 */
    @PostMapping("post")
    public RestBean<Void> insertPost(@RequestBody RestPostVO vo) {
        return ReturnUtils.messageHandle(vo, postServiceImpl::insertPost) ;
    }

    /** TODO: Written by - Han Yongding 2024/03/10 修改帖子 */
    @PutMapping("post")
    public RestBean<Void> updatePost(@RequestBody RestPostVO vo) {
        return ReturnUtils.messageHandle(vo, postServiceImpl::updatePost) ;
    }

    /** TODO: Written by - Han Yongding 2024/03/10 删除帖子 */
    @DeleteMapping("post/{postId}")
    public RestBean<Void> deletePost(@PathVariable("postId") String postId) {
        return ReturnUtils.messageHandle(() -> postServiceImpl.deletePost(postId)) ;
    }

    /* TODO: Written by - Han Yongding 2024/05/11 注入任务业务层实现类 */
    @Resource
    private TaskServiceImpl taskService;

    /* TODO: Written by - Han Yongding 2024/05/11 查询没有计划的任务，前端新增计划选择使用 */
    @GetMapping("task")
    public RestBean<Object> getAllTask() {
        return ReturnUtils.messageHandleData(taskService::getAllTasks);
    }

    /* TODO: Written by - Han Yongding 2024/05/11 注入任务计划业务层实现类 */
    @Resource
    private TaskPlanServiceImpl taskPlanService;

    /* TODO: Written by - Han Yongding 2024/05/11 查询所有任务计划 */
    @GetMapping("task-plan")
    public RestBean<Object> getAllTaskPlan(PageBean pageBean) {
        return ReturnUtils.messageHandleData(() -> taskPlanService.getAllTaskPlan(pageBean));
    }

    /* TODO: Written by - Han Yongding 2024/05/11 新增任务计划 */
    @PostMapping("task-plan")
    public RestBean<Void> insertTaskPlan(@RequestBody RestTaskPlanVO vo) {
        return ReturnUtils.messageHandle(vo, taskPlanService::insertTaskPlan);
    }

    /* TODO: Written by - Han Yongding 2024/05/11 修改任务计划 */
    @PutMapping("task-plan")
    public RestBean<Void> updateTaskPlan(@RequestBody RestTaskPlanVO vo) {
        return ReturnUtils.messageHandle(vo, taskPlanService::updateTaskPlan);
    }


    /* TODO: Written by - Han Yongding 2024/05/11 删除任务计划 */
    @DeleteMapping("task-plan/{id}")
    public RestBean<Void> deleteTaskPlan(@PathVariable("id") String id) {
        return ReturnUtils.messageHandle(() -> taskPlanService.deleteTaskPlan(id));
    }

    /* TODO: Written by - Han Yongding 2024/05/28 注入服务器参数业务层实现类 */
    @Resource
    private ServerParametersServiceImpl serverParametersService ;

    /* TODO: Written by - Han Yongding 2024/05/28 获取服务器参数 */
    @GetMapping("server-param")
    public RestBean<RespServerParametersVO> getServerParam() {
        return ReturnUtils.messageHandleData(serverParametersService::getServerParameters) ;
    }

    /* TODO: Written by - Han Yongding 2024/06/02 获取服务器服务信息 */
    @GetMapping("service-info")
    public RestBean<Object> getServerInfo() {
        return ReturnUtils.messageHandleData(serverParametersService::getServiceInformation);
    }

    /* TODO: Written by - Han Yongding 2024/06/09 注入网站流量表业务层实现类 */
    @Resource
    private WebsiteTrafficLogServiceImpl websiteTrafficService ;

    /* TODO: Written by - Han Yongding 2024/06/09 分页获取所有网站流量表 */
    @GetMapping("website-traffic")
    public RestBean<Object> getWebsiteTraffic(PageBean pageBean) {
        return ReturnUtils.messageHandleData(pageBean, websiteTrafficService::queryAllWebsiteTraffic);
    }

    /* TODO: Written by - Han Yongding 2024/06/16 注入登录日志业务层实现类 */
    @Resource
    private LoginLogServiceImpl loginLogService ;

    /* TODO: Written by - Han Yongding 2024/06/16 分页获取所有登录日志 */
    @GetMapping("login-log")
    public RestBean<Object> getLoginLog(PageBean pageBean) {
        return ReturnUtils.messageHandleData(pageBean, loginLogService::getAll);
    }

    /* TODO: Written by - Han Yongding 2024/06/16 注入请求日志业务层实现类 */
    @Resource
    private RequestLogServiceImpl requestLogService ;

    /* TODO: Written by - Han Yongding 2024/06/16 分页获取所有请求日志 */
    @GetMapping("request-log")
    public RestBean<Object> getRequestLog(PageBean pageBean) {
        return ReturnUtils.messageHandleData(pageBean, requestLogService::getAll) ;
    }

    /* TODO: Written by - Han Yongding 2024/06/27 注入日志业务层实现类 */
    @Resource
    private LogServiceImpl logService ;
    /* TODO: Written by - Han Yongding 2024/06/27 分页获取平台日志表 */
    @GetMapping("logs")
    public RestBean<Page<Log>> getLogs(PageBean pageBean) {
        return ReturnUtils.messageHandleData(pageBean, logService::getLogs) ;
    }

    /* TODO: Written by - Han Yongding 2024/06/29 备份日志表 */
    @PostMapping("logs")
    public RestBean<Void> backupLogs(@RequestBody RestLogVO vo) {
        return ReturnUtils.messageHandle(vo.getTableName(), logService::backupLogs) ;
    }

    /* TODO: Written by - Han Yongding 2024/07/02 清空日志表 */
    @DeleteMapping("logs/{tableName}/truncate")
    public RestBean<Void> truncateLogs(@PathVariable("tableName") String tableName) {
        return ReturnUtils.messageHandle(tableName, logService::truncateLogs) ;
    }

    /* TODO: Written by - Han Yongding 2024/07/02 分页获取备份日志表 */
    @GetMapping("backup-logs")
    public RestBean<Page<Log>> getBackupLogs(PageBean pageBean) {
        return ReturnUtils.messageHandleData(pageBean, logService::getBackupLogs);
    }

    /* TODO: Written by - Han Yongding 2024/07/02 导出日志表 */
    @GetMapping("backup-logs/{tableName}/export")
    public RestBean<Object> exportLogs(@PathVariable("tableName") String tableName) {
        return ReturnUtils.messageHandleData(tableName, logService::getBackupLogData);
    }

    /* TODO: Written by - Han Yongding 2024/07/02 批量导出日志表 */
    @GetMapping("backup-logs/export")
    public RestBean<Object> batchExportLogs(@RequestParam("ids") List<String> tableName) {
        return ReturnUtils.messageHandleData(tableName, logService::batchExportLogs);
    }

    /* TODO: Written by - Han Yongding 2024/07/02 删除备份日志表 */
    @DeleteMapping("backup-logs/{tableName}/drop")
    public RestBean<Void> dropBackupLogs(@PathVariable("tableName") String tableName) {
        return ReturnUtils.messageHandle(tableName, logService::dropBackupTableByName);
    }

    /* TODO: Written by - Han Yongding 2024/07/02 批量删除备份日志表 */
    @DeleteMapping("backup-logs/drop")
    public RestBean<Void> batchDropBackupLogs(@RequestParam("ids") List<String> tableName) {
        return ReturnUtils.messageHandle(tableName, logService::batchDropBackupLogs);
    }

    /* TODO: Written by - Han Yongding 2024/06/30 关键词业务层 */
    @Resource
    private SensitiveWordsServiceImpl sensitiveWordsService ;

    /* TODO: Written by - Han Yongding 2024/06/30 分页查询关键词 */
    @GetMapping("sensitive-words")
    public RestBean<Object> getSensitiveWords(PageBean pageBean) {
        return ReturnUtils.messageHandleData(pageBean, sensitiveWordsService::getAll);
    }

    /* TODO: Written by - Han Yongding 2024/06/30 新增关键词 */
    @PostMapping("sensitive-words")
    public RestBean<Void> insertSensitiveWords(@RequestBody RestSensitiveWordsVO vo) {
        return ReturnUtils.messageHandle(vo, sensitiveWordsService::add);
    }

    /* TODO: Written by - Han Yongding 2024/06/30 删除关键词 */
    @DeleteMapping("sensitive-words/{id}")
    public RestBean<Void> deleteSensitiveWords(@PathVariable("id") String id) {
        return ReturnUtils.messageHandle(id, sensitiveWordsService::delete);
    }

    /* TODO: Written by - Han Yongding 2024/07/01 批量删除关键词 */
    @DeleteMapping("sensitive-words")
    public RestBean<Void> deleteSensitiveWordsBatch(@RequestParam("ids") List<String> ids) {
        return ReturnUtils.messageHandle(ids, sensitiveWordsService::deleteBatch);
    }
}
