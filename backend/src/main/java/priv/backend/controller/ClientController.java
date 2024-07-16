package priv.backend.controller;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.kafka.support.KafkaUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import priv.backend.domain.PageBean;
import priv.backend.domain.PostPage;
import priv.backend.domain.RestBean;
import priv.backend.domain.es.dto.ESPost;
import priv.backend.domain.mongo.vo.RestMongoPostTypeVO;
import priv.backend.domain.vo.request.*;
import priv.backend.enumeration.MenuTypeEnum;
import priv.backend.service.es.impl.ESClientUserServiceImpl;
import priv.backend.service.es.impl.ESPostServiceImpl;
import priv.backend.service.impl.*;
import priv.backend.util.KafkaProducerUtils;
import priv.backend.util.ReturnUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 客户端控制器
 * @CreateDate :  2024-03-30 19:13
 */
@RestController
@RequestMapping("/api/index/")
public class ClientController {

    /* TODO: Written by - Han Yongding 2024/04/03 注入帖子类型业务层 */
    private final TypesPostServiceImpl typesPostService ;

    /* TODO: Written by - Han Yongding 2024/04/03 注入帖子业务层 */
    private final PostServiceImpl postService ;

    /* TODO: Written by - Han Yongding 2024/04/04 注入帖子ES业务层 */
    private final ESPostServiceImpl esPostService ;

    /* TODO: Written by - Han Yongding 2024/04/07 注入用户业务层实现类 */
    private final UserServiceImpl userService ;

    /* TODO: Written by - Han Yongding 2024/04/08 注入用户等级信息业务层实现类 */
    private final UserLevelServiceImpl userLevelService;

    @Autowired
    public ClientController(TypesPostServiceImpl typesPostService,
                            PostServiceImpl postService,
                            ESPostServiceImpl esPostService,
                            UserServiceImpl userService,
                            UserLevelServiceImpl userLevelService) {
        this.typesPostService = typesPostService ;
        this.postService = postService ;
        this.esPostService = esPostService ;
        this.userService = userService ;
        this.userLevelService = userLevelService ;
    }

    /* TODO: Written by - Han Yongding 2024/04/02 获取帖子类型 */
    @GetMapping("post-types")
    public RestBean<Object> getPostTypes() {
        return ReturnUtils.messageHandleData(typesPostService::getPostTypes) ;
    }

    /* TODO: Written by - Han Yongding 2024/04/02 新增帖子，分享中心分享 */
    @PostMapping("/post")
    public RestBean<Void> addPost( @RequestParam(value = "files", required = false) List<MultipartFile> files,
                                   @RequestParam("userId") String userId,
                                   @RequestParam("topic") String topic,
                                   @RequestParam("content") String content,
                                   @RequestParam("typeId") String typeId) {
        RestPostsVO vo = new RestPostsVO() ;
        vo.setUserId(userId);
        vo.setTopic(topic);
        vo.setContent(content);
        vo.setTypeId(typeId);
        vo.setList(files);

        // 处理请求
        return ReturnUtils.messageHandle(vo, postService::insertPost);
    }

    /* TODO: Written by - Han Yongding 2024/07/11 搜索建议 */
    @GetMapping("search-suggest")
    public RestBean<Object> searchSuggest(String queryString) {
        return ReturnUtils.messageHandleData(() -> esPostService.searchSuggest(queryString)) ;
    }

    /* TODO: Written by - Han Yongding 2024/04/04 分页查询帖子 ES */
    @GetMapping("post-es")
    public RestBean<Page<ESPost>> getPostEs(PageBean pageBean, String userId, String type, @RequestParam(required = false) String menuId) {

        return ReturnUtils.messageHandleData(new PostPage(pageBean, userId, type, menuId), esPostService::getAllESPost) ;
    }
    /* TODO: Written by - Han Yongding 2024/07/12 客户端用户业务层实现类 */
    @Resource
    private ESClientUserServiceImpl clientUserService ;

    /* TODO: Written by - Han Yongding 2024/07/12 搜索 */
    @GetMapping("search")
    public RestBean<Object> search(PageBean pageBean, String type, String queryString) {
        /* TODO: Written by - Han Yongding 2024/07/12 帖子搜索 */
        if (type.equals(MenuTypeEnum.POST.toString())) {
            return ReturnUtils.messageHandleData(() -> esPostService.search(queryString, pageBean));
        } else {
            /* TODO: Written by - Han Yongding 2024/07/12 用户搜索 */
            return ReturnUtils.messageHandleData(() -> clientUserService.searchByNickname(queryString, pageBean));
        }
    }


    /* TODO: Written by - Han Yongding 2024/04/07 根据用户ID查询相应信息 ES */
    @GetMapping("user/{userId}")
    public RestBean<Object> getClientUserByUserId(@PathVariable("userId")String userId) {
        return ReturnUtils.messageHandleData(() -> userService.getClientUserById(userId)) ;
    }

    /* TODO: Written by - Han Yongding 2024/04/08 查询等级信息 */
    @GetMapping("level")
    public RestBean<Object> getLevel() {
        return ReturnUtils.messageHandleData(userLevelService::getAllUserLevel) ;
    }

    /* TODO: Written by - Han Yongding 2024/04/10 客户端修改个人信息 */
    @PutMapping("user")
    public RestBean<Void> updateUser(@ModelAttribute RestClientUserVO vo) {
        return ReturnUtils.messageHandle(vo, userService::updateClientUserById) ;
    }

    /* TODO: Written by - Han Yongding 2024/04/15 客户端点赞 */
    @PutMapping("like")
    public RestBean<Void> like(@RequestBody @Validated RestCountVO vo) {
        return ReturnUtils.messageHandle(vo, postService::likePost) ;
    }

    /* TODO: Written by - Han Yongding 2024/04/30 发布评论 */
    @PostMapping("comment")
    public RestBean<String> addComment(@RequestBody @Validated RestCommentVO vo) {
        return ReturnUtils.messageHandleData(vo, postService::commentPost) ;
    }

    /* TODO: Written by - Han Yongding 2024/05/07 收集用户喜好数据  */
    @PostMapping("collect-preference")
    public RestBean<Void> collectPreference(@RequestBody @Validated RestMongoPostTypeVO vo) {
        return ReturnUtils.messageHandle(vo, userService::collectUserHobby) ;
    }

    /* TODO: Written by - Han Yongding 2024/07/08 注入帖子点击量业务层实现类 */
    @Resource
    private PostHitsServiceImpl postHitsService ;

    /* TODO: Written by - Han Yongding 2024/07/08 帖子点击量统计 */
    @PostMapping("post-hits")
    public RestBean<Void> postHits(@RequestBody List<RestPostHits> postIds) {
        return ReturnUtils.messageHandle(postIds, postHitsService::postHitsByPostId);
    }
}
