package priv.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import priv.backend.domain.RestBean;
import priv.backend.domain.vo.request.RestPostVO;
import priv.backend.domain.vo.request.RestPostsVO;
import priv.backend.domain.vo.response.RespPostVO;
import priv.backend.service.impl.PostServiceImpl;
import priv.backend.service.impl.TypesPostServiceImpl;
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

    @Autowired
    public ClientController(TypesPostServiceImpl typesPostService,
                            PostServiceImpl postService) {
        this.typesPostService = typesPostService ;
        this.postService = postService ;
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
}
