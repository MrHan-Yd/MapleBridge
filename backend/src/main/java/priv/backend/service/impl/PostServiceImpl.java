package priv.backend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import priv.backend.domain.Files;
import priv.backend.domain.PageBean;
import priv.backend.domain.dto.FilePost;
import priv.backend.domain.dto.Post;
import priv.backend.domain.es.dto.ESPost;
import priv.backend.domain.vo.request.RestPostVO;
import priv.backend.domain.vo.request.RestPostsVO;
import priv.backend.domain.vo.response.RespPostVO;
import priv.backend.mapper.PostMapper;
import priv.backend.service.PostService;
import priv.backend.util.CurrentUtils;
import priv.backend.util.LogUtils;
import priv.backend.util.UploadUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 帖子业务层实现类
 * @CreateDate :  2024-03-08 15:19
 */
@Service
public class PostServiceImpl implements PostService {

    /* TODO: Written by - Han Yongding 2024/03/09 注入帖子DAO */
    @Resource
    private PostMapper mapper ;

    /* TODO: Written by - Han Yongding 2024/04/03 注入帖子文件表业务层 */
    @Resource
    private FilePostServiceImpl filePostService ;

    /* TODO: Written by - Han Yongding 2024/04/03 注入上载工具类 */
    @Resource
    private UploadUtils uploadUtils ;

    /* TODO: Written by - Han Yongding 2024/04/03 注入RabbitMQ 模板 */
    @Resource
    private AmqpTemplate amqpTemplate ;

    /** TODO: Written by - Han Yongding 2024/03/09 分页查询所有帖子 */
    @Override
    public Page<RespPostVO> getPagePost(PageBean pageBean){
        Page<RespPostVO> page = new Page<>(pageBean.getPageNum(), pageBean.getPageSize()) ;

        /* TODO: Written by - Han Yongding 2024/03/09 处理分页信息和数据并返回 */
        return mapper.getPagePost(page);
    }

    /** TODO: Written by - Han Yongding 2024/03/09 新增帖子 */
    @Override
    public String insertPost(RestPostVO vo){
        if (vo == null) {
            return "数据为空" ;
        }
        /* TODO: Written by - Han Yongding 2024/03/09 初始化 */
        Post viewObject = vo.asViewObject(Post.class);
        viewObject.setTimestamp(CurrentUtils.getTheCurrentSystemTime()) ;
        viewObject.setLikeCount("0");
        viewObject.setCommentCount("0");

        /* TODO: Written by - Han Yongding 2024/03/09 新增失败 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.insert(viewObject))) {
            return "新增失败，请稍后再试"  ;
        }

        /* TODO: Written by - Han Yongding 2024/03/09 新增成功 */
        return null ;
    }

    /* TODO: Written by - Han Yongding 2024/04/03 编程式事务，用于控制前端发布帖子，插入帖子表和帖子文件表的控制 */
    @Resource
    private TransactionTemplate insertPostTemplate ;

    /* TODO: Written by - Han Yongding 2024/04/03 前端发布帖子，可能会上传图片资源 */
    @Override
    public String insertPost(RestPostsVO vo) {
        return insertPostTemplate.execute(status -> {
            if (vo == null) {
                return "数据为空，请稍后再试" ;
            }

            /* TODO: Written by - Han Yongding 2024/04/03 表单数据存入数据库 */
            Post viewObject = vo.asViewObject(Post.class);
            int insert = mapper.insert(viewObject);
            /* TODO: Written by - Han Yongding 2024/04/03 没有插入成功，也没有生成唯一标识 */
            if (insert == 0 && viewObject.getPostId() == null) {
                /* TODO: Written by - Han Yongding 2024/04/03 插入失败，业务结束 */
                return "帖子插入失败" ;
            }

            /* TODO: Written by - Han Yongding 2024/04/03 如果有图片则写出文件，最少有一张图片的意思 */
            if (vo.getList() == null) {
                /* TODO: Written by - Han Yongding 2024/04/03 将PostId传递给RabbitMq，由它去消费同步到ES中 */
                amqpTemplate.convertAndSend("postSyncES", viewObject.getPostId()) ;
                /* TODO: Written by - Han Yongding 2024/04/03 没有图片，不需要写出，业务结束(发布成功) */
                return null ;
            }

            /* TODO: Written by - Han Yongding 2024/04/03 写出文件，获取文件名、文件类型 */
            /* TODO: Written by - Han Yongding 2024/04/03 获取写出路径 */
            String path = uploadUtils.generateSavePath(viewObject.getUserId(), "post/" +viewObject.getPostId());
            /* TODO: Written by - Han Yongding 2024/04/03 批量写出 */
            List<Files> filesList = new ArrayList<>();
            /* TODO: Written by - Han Yongding 2024/04/03 写出文件 */
            try {
                filesList = uploadUtils.batchSaveFile(vo.getList(), path);
            } catch (IOException e) {
                LogUtils.warning(this.getClass(), "文件写出失败");
            }
            /* TODO: Written by - Han Yongding 2024/04/03 写出失败，回滚已插入数据库中的数据 */
            if (filesList == null) {
                /* TODO: Written by - Han Yongding 2024/04/03 回滚 */
                status.setRollbackOnly();
                return "写出失败" ;
            }

            /* TODO: Written by - Han Yongding 2024/04/03 帖子文件表，批量写入数据库 */
            Integer rs = this.batchInsertFilePost(
                    filesList.stream().map(file -> {
                        FilePost viewObject1 = file.asViewObject(FilePost.class);
                        viewObject1.setPostId(viewObject.getPostId());
                        return viewObject1 ;
                    }).toList());

            /* TODO: Written by - Han Yongding 2024/04/03 影响行数 */
            if (rs == 0) {
                /* TODO: Written by - Han Yongding 2024/04/03 数据会滚了，删除写出的数据 */
                boolean post = uploadUtils.deleteFile(uploadUtils.generateDeletePath(viewObject.getUserId() + "/post", viewObject.getPostId()));
                if (post) {
                    LogUtils.info(this.getClass(), "PostFile表数据插入失败，已回滚。删除写出文件成功") ;
                } else {
                    LogUtils.info(this.getClass(), "PostFile表数据插入失败，已回滚。删除写出文件失败，请管理员手动清除，路径:" + viewObject.getUserId() + "/post/" + viewObject.getPostId()) ;
                }
                /* TODO: Written by - Han Yongding 2024/04/03 回滚 */
                status.setRollbackOnly();
                return "帖子文件表插入失败" ;
            }

            /* TODO: Written by - Han Yongding 2024/04/03 将PostId传递给RabbitMq，由它去消费同步到ES中 */
            amqpTemplate.convertAndSend("postSyncES", viewObject.getPostId()) ;
            /* TODO: Written by - Han Yongding 2024/04/03 插入成功，业务完成 */
            return null ;
        }) ;

    }

    /* TODO: Written by - Han Yongding 2024/04/03 插入帖子表 */
    private Integer batchInsertFilePost(List<FilePost> list) {
        return insertPostTemplate.execute(status -> {
            if (list.isEmpty()) {
                return 0 ;
            }
            Integer count = filePostService.batchInsertFilePost(list);
            if (count <= 0) {
                /* TODO: Written by - Han Yongding 2024/04/03 没有插入数据，回滚 */
                status.setRollbackOnly();
            }
            return count ;
        }) ;
    }

    /** TODO: Written by - Han Yongding 2024/03/10 修改帖子 */
    @Override
    public String updatePost(RestPostVO vo){
        if (vo == null) {
            return "数据为空" ;
        }

        /* TODO: Written by - Han Yongding 2024/03/10 修改失败 */
        /* TODO: Written by - Han Yongding 2024/03/09 新增失败 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.updateById(vo.asViewObject(Post.class)))) {
            return "修改失败，请稍后再试"  ;
        }

        /* TODO: Written by - Han Yongding 2024/03/10 修改成功 */
        return null ;
    }

    /** TODO: Written by - Han Yongding 2024/03/10 删除帖子 */
    @Override
    public String deletePost(String postId) {
        if (postId == null) {
            return "唯一标识为空" ;
        }

        /* TODO: Written by - Han Yongding 2024/03/10 删除失败 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.deleteById(postId))) {
            return "删除失败，请稍后再试" ;
        }

        /* TODO: Written by - Han Yongding 2024/03/10 删除成功 */
        return null ;
    }

    /** TODO: Written by - Han Yongding 2024/04/01 查询所有帖子同步ES使用 */
    @Override
    public List<ESPost> getAllPostSyncES() {
        return mapper.getAllPostSyncES() ;
    }

    /* TODO: Written by - Han Yongding 2024/04/03 根据postId查询帖子数据，同步ES使用 */
    @Override
    public ESPost getPostByPostIdSyncES(String postId) {
        return mapper.getPostByPostIdSyncES(postId) ;
    }
}
