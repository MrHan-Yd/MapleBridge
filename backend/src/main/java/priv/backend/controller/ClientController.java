package priv.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import priv.backend.domain.PageBean;
import priv.backend.domain.RestBean;
import priv.backend.domain.es.dto.ESPost;
import priv.backend.domain.vo.request.RestClientUserVO;
import priv.backend.domain.vo.request.RestPostsVO;
import priv.backend.service.es.impl.ESPostServiceImpl;
import priv.backend.service.impl.PostServiceImpl;
import priv.backend.service.impl.TypesPostServiceImpl;
import priv.backend.service.impl.UserLevelServiceImpl;
import priv.backend.service.impl.UserServiceImpl;
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
    public RestBean<Void> addPost( List<MultipartFile> files,
                                   String userId,
                                   String topic,
                                   String content,
                                   String typeId) {
        RestPostsVO vo = new RestPostsVO() ;
        vo.setUserId(userId);
        vo.setTopic(topic);
        vo.setContent(content);
        vo.setTypeId(typeId);
        vo.setList(files);

        // 处理请求
        return ReturnUtils.messageHandle(vo, postService::insertPost);
    }

    /* TODO: Written by - Han Yongding 2024/04/04 分页查询帖子 ES */
    @GetMapping("post-es")
    public RestBean<Page<ESPost>> getPostEs(PageBean pageBean) {
        return ReturnUtils.messageHandleData(pageBean, esPostService::getAllESPost) ;
    }

    /* TODO: Written by - Han Yongding 2024/04/07 根据用户ID查询相应信息 */
    @GetMapping("user/{userId}")
    public RestBean<Object> getClientUserByUserId(@PathVariable("userId")String userId) {
        return ReturnUtils.messageHandleData(() -> userService.getClientUserById(userId)) ;
    }

    /* TODO: Written by - Han Yongding 2024/04/08 查询等级信息 */
    @GetMapping("level")
    public RestBean<Object> getLevel() {
        return ReturnUtils.messageHandleData(userLevelService::getAllUserLevel) ;
    }

    /* TODO: Written by - Han Yongding 2024/04/10 前端修改个人信息 */
    @PutMapping("user")
    public RestBean<Void> updateUser(@ModelAttribute RestClientUserVO vo) {
        return ReturnUtils.messageHandle(vo, userService::updateClientUserById) ;
    }
}
