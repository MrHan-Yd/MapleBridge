package priv.backend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import priv.backend.domain.Files;
import priv.backend.domain.PageBean;
import priv.backend.domain.dto.Comment;
import priv.backend.domain.dto.FilePost;
import priv.backend.domain.dto.Like;
import priv.backend.domain.dto.Post;
import priv.backend.domain.es.dto.ESPost;
import priv.backend.domain.vo.request.RestCommentVO;
import priv.backend.domain.vo.request.RestCountVO;
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
import java.util.Objects;

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
    private PostMapper mapper;

    /* TODO: Written by - Han Yongding 2024/04/03 注入帖子文件表业务层 */
    @Resource
    private FilePostServiceImpl filePostService;

    /* TODO: Written by - Han Yongding 2024/04/03 注入上载工具类 */
    @Resource
    private UploadUtils uploadUtils;

    /* TODO: Written by - Han Yongding 2024/04/03 注入RabbitMQ 模板 */
    @Resource
    private AmqpTemplate amqpTemplate;

    /**
     * TODO: Written by - Han Yongding 2024/03/09 分页查询所有帖子
     */
    @Override
    public Page<RespPostVO> getPagePost(PageBean pageBean) {
        Page<RespPostVO> page = new Page<>(pageBean.getPageNum(), pageBean.getPageSize());

        /* TODO: Written by - Han Yongding 2024/03/09 处理分页信息和数据并返回 */
        return mapper.getPagePost(page);
    }

    /**
     * TODO: Written by - Han Yongding 2024/03/09 新增帖子
     */
    @Override
    public String insertPost(RestPostVO vo) {
        if (vo == null) {
            return "数据为空";
        }
        /* TODO: Written by - Han Yongding 2024/03/09 初始化 */
        Post viewObject = vo.asViewObject(Post.class);
        viewObject.setTimestamp(CurrentUtils.getTheCurrentSystemTime());
        viewObject.setLikeCount("0");
        viewObject.setCommentCount("0");

        /* TODO: Written by - Han Yongding 2024/03/09 新增失败 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.insert(viewObject))) {
            return "新增失败，请稍后再试";
        }

        /* TODO: Written by - Han Yongding 2024/03/09 新增成功 */
        return null;
    }

    /* TODO: Written by - Han Yongding 2024/04/03 编程式事务，用于控制前端发布帖子，插入帖子表和帖子文件表的控制 */
    @Resource
    private TransactionTemplate insertPostTemplate;

    /* TODO: Written by - Han Yongding 2024/04/03 前端发布帖子，可能会上传图片资源 */
    @Override
    public String insertPost(RestPostsVO vo) {
        return insertPostTemplate.execute(status -> {
            if (vo == null) {
                return "数据为空，请稍后再试";
            }

            /* TODO: Written by - Han Yongding 2024/04/03 表单数据存入数据库 */
            Post viewObject = vo.asViewObject(Post.class);
            int insert = mapper.insert(viewObject);
            /* TODO: Written by - Han Yongding 2024/04/03 没有插入成功，也没有生成唯一标识 */
            if (insert == 0 && viewObject.getPostId() == null) {
                /* TODO: Written by - Han Yongding 2024/04/03 插入失败，业务结束 */
                return "帖子插入失败";
            }

            /* TODO: Written by - Han Yongding 2024/04/03 如果有图片则写出文件，最少有一张图片的意思 */
            if (vo.getList() == null) {
                /* TODO: Written by - Han Yongding 2024/04/03 将PostId传递给RabbitMq，由它去消费同步到ES中 */
                amqpTemplate.convertAndSend("postSyncES", viewObject.getPostId());
                /* TODO: Written by - Han Yongding 2024/04/03 没有图片，不需要写出，业务结束(发布成功) */
                return null;
            }

            /* TODO: Written by - Han Yongding 2024/04/03 写出文件，获取文件名、文件类型 */
            /* TODO: Written by - Han Yongding 2024/04/03 获取写出路径 */
            String path = uploadUtils.generateSavePath(viewObject.getUserId(), "post/" + viewObject.getPostId());
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
                return "写出失败";
            }

            /* TODO: Written by - Han Yongding 2024/04/03 帖子文件表，批量写入数据库 */
            Integer rs = this.batchInsertFilePost(
                    filesList.stream().map(file -> {
                        FilePost viewObject1 = file.asViewObject(FilePost.class);
                        viewObject1.setPostId(viewObject.getPostId());
                        return viewObject1;
                    }).toList());

            /* TODO: Written by - Han Yongding 2024/04/03 影响行数 */
            if (rs == 0) {
                /* TODO: Written by - Han Yongding 2024/04/03 数据会滚了，删除写出的数据 */
                boolean post = uploadUtils.deleteFile(uploadUtils.generateDeletePath(viewObject.getUserId() + "/post", viewObject.getPostId()));
                if (post) {
                    LogUtils.info(this.getClass(), "PostFile表数据插入失败，已回滚。删除写出文件成功");
                } else {
                    LogUtils.info(this.getClass(), "PostFile表数据插入失败，已回滚。删除写出文件失败，请管理员手动清除，路径:" + viewObject.getUserId() + "/post/" + viewObject.getPostId());
                }
                /* TODO: Written by - Han Yongding 2024/04/03 回滚 */
                status.setRollbackOnly();
                return "帖子文件表插入失败";
            }

            /* TODO: Written by - Han Yongding 2024/04/03 将PostId传递给RabbitMq，由它去消费同步到ES中 */
            amqpTemplate.convertAndSend("postSyncES", viewObject.getPostId());
            /* TODO: Written by - Han Yongding 2024/04/03 插入成功，业务完成 */
            return null;
        });

    }

    /* TODO: Written by - Han Yongding 2024/04/03 插入帖子表 */
    private Integer batchInsertFilePost(List<FilePost> list) {
        return insertPostTemplate.execute(status -> {
            if (list.isEmpty()) {
                return 0;
            }
            Integer count = filePostService.batchInsertFilePost(list);
            if (count <= 0) {
                /* TODO: Written by - Han Yongding 2024/04/03 没有插入数据，回滚 */
                status.setRollbackOnly();
            }
            return count;
        });
    }

    /**
     * TODO: Written by - Han Yongding 2024/03/10 修改帖子
     */
    @Override
    public String updatePost(RestPostVO vo) {
        if (vo == null) {
            return "数据为空";
        }

        /* TODO: Written by - Han Yongding 2024/03/10 修改失败 */
        /* TODO: Written by - Han Yongding 2024/03/09 新增失败 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.updateById(vo.asViewObject(Post.class)))) {
            return "修改失败，请稍后再试";
        }

        /* TODO: Written by - Han Yongding 2024/03/10 修改成功 */
        return null;
    }

    /**
     * TODO: Written by - Han Yongding 2024/03/10 删除帖子
     */
    @Override
    public String deletePost(String postId) {
        if (postId == null) {
            return "唯一标识为空";
        }

        /* TODO: Written by - Han Yongding 2024/03/10 删除失败 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.deleteById(postId))) {
            return "删除失败，请稍后再试";
        }

        /* TODO: Written by - Han Yongding 2024/03/10 删除成功 */
        return null;
    }

    /**
     * TODO: Written by - Han Yongding 2024/04/01 查询所有帖子同步ES使用
     */
    @Override
    public List<ESPost> getAllPostSyncES() {
        return mapper.getAllPostSyncES();
    }

    /* TODO: Written by - Han Yongding 2024/04/03 根据postId查询帖子数据，同步ES使用 */
    @Override
    public ESPost getPostByPostIdSyncES(String postId) {
        return mapper.getPostByPostIdSyncES(postId);
    }

    /* TODO: Written by - Han Yongding 2024/04/15 根据ID查询帖子版本号和点赞数量 */
    @Override
    public Post getLikeAndVersionByPostId(String postId) {
        return mapper.getLikeAndVersionByPostId(postId);
    }

    /* TODO: Written by - Han Yongding 2024/04/15 根据版本号和PostId更新帖子点赞数量 */
    @Override
    public Boolean updateLikeAndVersionByPostId(RestCountVO count) {
        return mapper.updateLikeAndVersionByPostId(count) == 1;
    }

    /* TODO: Written by - Han Yongding 2024/04/15 根据版本号和PostId更新帖子点赞数量 */
    @Override
    public Boolean updateUnLikeAndVersionByPostId(RestCountVO count) {
        return mapper.updateUnLikeAndVersionByPostId(count) == 1;
    }

    /* TODO: Written by - Han Yongding 2024/04/15 注入点赞业务层实现类 */
    @Resource
    private LikeServiceImpl likeService;

    /* TODO: Written by - Han Yongding 2024/04/15 点赞帖子事务 */
    @Resource
    private TransactionTemplate likePostTemplate;

    /* TODO: Written by - Han Yongding 2024/04/15 取消点赞事务 */
    @Resource
    private TransactionTemplate unlikePostTemplate;

    /* TODO: Written by - Han Yongding 2024/04/15 注入点赞重试队列 */
    @Resource
    private AmqpTemplate likeTemplate ;


    /* TODO: Written by - Han Yongding 2024/04/15 点赞帖子 */
    @Override
    public String likePost(RestCountVO vo) {
        if (vo == null) {
            return "数据为空，请稍候重试";
        }
        if ("like".equals(vo.getType())) {
            return likePostTemplate.execute(status -> {

                /* TODO: Written by - Han Yongding 2024/04/15 插入Like表失败 */
                if (this.insertLike(vo) != null) {
                    /* TODO: Written by - Han Yongding 2024/04/15 尝试回滚 */
                    status.setRollbackOnly();
                    return "点赞失败，请稍候重试";
                }

                /* TODO: Written by - Han Yongding 2024/05/05 判断是点赞评论还是帖子 */
                if (vo.getCommentId() != null) {
                    if (updateCommentLikeCountAndVersion(vo) != null) {
                        /* TODO: Written by - Han Yongding 2024/05/05 尝试回滚 */
                        status.setRollbackOnly();
                        return "点赞失败，请稍候重试";
                    }
                } else {
                    /* TODO: Written by - Han Yongding 2024/04/15 点赞表插入成功，更新帖子表点赞数量和版本号 */
                    if (updatePostLikeCountAndVersion(vo) != null) {
                        /* TODO: Written by - Han Yongding 2024/04/15 尝试回滚 */
                        status.setRollbackOnly();
                        return "点赞失败，请稍候重试";
                    }
                }


                /* TODO: Written by - Han Yongding 2024/04/15 成功则同步更新单条数据 */
                amqpTemplate.convertAndSend("postSyncES", vo.getId()) ;

                /* TODO: Written by - Han Yongding 2024/04/15 点赞成功, 业务结束 */
                return null ;
            });
        } else {
            return unlikePostTemplate.execute(status -> {
                /* TODO: Written by - Han Yongding 2024/04/15 取消点赞 */

                /* TODO: Written by - Han Yongding 2024/05/05 判断是取消点赞评论还是帖子 */
                if (vo.getCommentId() != null) {
                    /* TODO: Written by - Han Yongding 2024/05/05 删除点赞记录失败 */
                    if (this.deleteLikeByCommentIdAndUserId(vo.getCommentId(), vo.getUserId()) != null) {
                        /* TODO: Written by - Han Yongding 2024/04/15 尝试回滚 */
                        status.setRollbackOnly();
                        return "取消点赞失败，请稍候重试";
                    }

                    /* TODO: Written by - Han Yongding 2024/05/05 更新点赞数量和版本号 */
                    if (updateUnCommentCountAndVersion(vo) != null) {
                        /* TODO: Written by - Han Yongding 2024/04/15 尝试回滚 */
                        status.setRollbackOnly();
                        return "取消点赞失败，请稍候重试";
                    }
                } else {
                    /* TODO: Written by - Han Yongding 2024/04/15 删除点赞记录失败 */
                    if (this.deleteLikeByPostIdAndUserId(vo.getId(), vo.getUserId()) != null) {
                        /* TODO: Written by - Han Yongding 2024/04/15 尝试回滚 */
                        status.setRollbackOnly();
                        return "取消点赞失败，请稍候重试";
                    }

                    /* TODO: Written by - Han Yongding 2024/04/15 点赞表插入成功，更新帖子表点赞数量和版本号 */
                    if (updatePostUnLikeCountAndVersion(vo) != null) {
                        /* TODO: Written by - Han Yongding 2024/04/15 尝试回滚 */
                        status.setRollbackOnly();
                        return "取消点赞失败，请稍候重试";
                    }
                }

                /* TODO: Written by - Han Yongding 2024/04/15 成功则同步更新单条数据 */
                amqpTemplate.convertAndSend("postSyncES", vo.getId()) ;

                /* TODO: Written by - Han Yongding 2024/04/15 取消点赞成功，业务结束 */
                return null ;
            }) ;
        }
    }

    /* TODO: Written by - Han Yongding 2024/04/15 更新post表点赞数量和版本号 */
    private String updatePostLikeCountAndVersion(RestCountVO vo) {
        return likePostTemplate.execute(status -> {
            if (vo == null) {
                return "数据为空";
            }

            boolean updated = false;
            int maxRetries = 3;
            int retries = 0;

            /* TODO: Written by - Han Yongding 2024/04/25 使用前端传递过来的版本号进行更新，如果更新失败则重试 */
            while (!updated && retries < maxRetries) {
                // 尝试更新
                updated = this.updateLikeAndVersionByPostId(vo);

                if (!updated) {
                    // 更新失败，尝试获取最新的版本号并重新更新
                    Post post = this.getLikeAndVersionByPostId(vo.getId());
                    if (post != null) {
                        vo.setVersion(post.getVersion());
                    }
                    /* TODO: Written by - Han Yongding 2024/04/15 重试次数增加 */
                    retries++;
                    try {
                        Thread.sleep(200); // 重试间隔，可以根据需要调整
                    } catch (InterruptedException e) {
                        /* TODO: Written by - Han Yongding 2024/04/15 回滚数据 */
                        status.setRollbackOnly();
                        /* TODO: Written by - Han Yongding 2024/04/15 终止线程 */
                        Thread.currentThread().interrupt();
                    }
                }
            }

            /* TODO: Written by - Han Yongding 2024/04/15 更新失败，扔到队列中让队列处理 */
            if (!updated) {
                likeTemplate.convertAndSend("postUnLikeRetry", vo) ;
                return "点赞失败，请稍候重试";
            }

            /* TODO: Written by - Han Yongding 2024/04/15 更新成功 */
            return null;
        });
    }

    /* TODO: Written by - Han Yongding 2024/05/05 注入评论表业务层 */
    @Resource
    private CommentServiceImpl commentService;

    /* TODO: Written by - Han Yongding 2024/04/15 更新评论表表点赞数量和版本号 */
    private String updateCommentLikeCountAndVersion(RestCountVO vo) {
        return likePostTemplate.execute(status -> {
            if (vo == null) {
                return "数据为空";
            }

            boolean updated = false;
            int maxRetries = 3;
            int retries = 0;

            /* TODO: Written by - Han Yongding 2024/04/25 使用前端传递过来的版本号进行更新，如果更新失败则重试 */
            while (!updated && retries < maxRetries) {
                // 尝试更新
                updated = commentService.updateCommentAndVersionById(vo);

                if (!updated) {
                    // 更新失败，尝试获取最新的版本号并重新更新
                    Comment comment = commentService.getCommentLikeCountById(vo.getCommentId());
                    if (comment != null) {
                        vo.setVersion(comment.getVersion());
                    }
                    /* TODO: Written by - Han Yongding 2024/04/15 重试次数增加 */
                    retries++;
                    try {
                        Thread.sleep(200); // 重试间隔，可以根据需要调整
                    } catch (InterruptedException e) {
                        /* TODO: Written by - Han Yongding 2024/04/15 回滚数据 */
                        status.setRollbackOnly();
                        /* TODO: Written by - Han Yongding 2024/04/15 终止线程 */
                        Thread.currentThread().interrupt();
                    }
                }
            }

            /* TODO: Written by - Han Yongding 2024/04/15 更新失败，扔到队列中让队列处理 */
            if (!updated) {
                likeTemplate.convertAndSend("commentLikeRetry", vo) ;
                return "点赞失败，请稍候重试";
            }

            /* TODO: Written by - Han Yongding 2024/04/15 更新成功 */
            return null;
        });
    }

    /* TODO: Written by - Han Yongding 2024/04/15 更新评论表表点赞数量和版本号 */
    private String updateUnCommentCountAndVersion(RestCountVO vo) {
        return likePostTemplate.execute(status -> {
            if (vo == null) {
                return "数据为空";
            }

            boolean updated = false;
            int maxRetries = 3;
            int retries = 0;

            // 使用前端传递过来的版本号进行更新，如果更新失败则重试
            while (!updated && retries < maxRetries) {
                // 尝试更新
                updated = commentService.updateUnCommentAndVersionById(vo);

                if (!updated) {
                    // 更新失败，尝试获取最新的版本号并重新更新
                    Comment comment = commentService.getCommentLikeCountById(vo.getCommentId());
                    if (comment != null) {
                        vo.setVersion(comment.getVersion());
                    }
                    /* TODO: Written by - Han Yongding 2024/04/15 重试次数增加 */
                    retries++;
                    try {
                        Thread.sleep(200); // 重试间隔，可以根据需要调整
                    } catch (InterruptedException e) {
                        /* TODO: Written by - Han Yongding 2024/04/15 回滚数据 */
                        status.setRollbackOnly();
                        /* TODO: Written by - Han Yongding 2024/04/15 终止线程 */
                        Thread.currentThread().interrupt();
                    }
                }
            }

            /* TODO: Written by - Han Yongding 2024/04/15 更新失败，扔到队列中让队列处理 */
            if (!updated) {
                likeTemplate.convertAndSend("commentUnLikeRetry", vo) ;
                return "取消点赞失败，请稍候重试";
            }

            /* TODO: Written by - Han Yongding 2024/04/15 更新成功 */
            return null;
        });
    }

    /* TODO: Written by - Han Yongding 2024/04/15 插入点赞表 */
    @Override
    public String insertLike(RestCountVO vo) {
        return likePostTemplate.execute(status -> {
            /* TODO: Written by - Han Yongding 2024/04/15 数据为空，不能插入 */
            if (vo == null) {
                return "数据为空";
            }

            Like like = vo.asViewObject(Like.class);
            like.setPostId(vo.getId());
            like.setTimestamp(CurrentUtils.getTheCurrentSystemTime());
            boolean save = likeService.insertPostLike(like);

            /* TODO: Written by - Han Yongding 2024/04/15 插入失败 */
            if (!save) {
                return "数据插入失败";
            }

            /* TODO: Written by - Han Yongding 2024/04/15 插入成功 */
            return null;
        });
    }

    /* TODO: Written by - Han Yongding 2024/04/15 删除点赞记录 */
    @Override
    public String deleteLikeByPostIdAndUserId(String postId, String userId) {
        return unlikePostTemplate.execute(status -> {
            /* TODO: Written by - Han Yongding 2024/04/15 数据为空，不能插入 */
            if (postId == null || userId == null) {
                return "数据为空";
            }
            if (!likeService.deleteLikeByPostIdAndUserId(postId, userId)) {
                return "删除失败";
            }

            /* TODO: Written by - Han Yongding 2024/04/15 删除成功 */
            return null;
        }) ;
    }

    /* TODO: Written by - Han Yongding 2024/04/15 删除点赞记录 */
    @Override
    public String deleteLikeByCommentIdAndUserId(String commentId, String userId) {
        return unlikePostTemplate.execute(status -> {
            /* TODO: Written by - Han Yongding 2024/04/15 数据为空，不能插入 */
            if (commentId == null || userId == null) {
                return "数据为空";
            }
            if (!likeService.deleteLikeByCommentIdAndUserId(commentId, userId)) {
                return "删除失败";
            }

            /* TODO: Written by - Han Yongding 2024/04/15 删除成功 */
            return null;
        }) ;
    }

    /* TODO: Written by - Han Yongding 2024/04/15 更新post表点赞数量和版本号 */
    private String updatePostUnLikeCountAndVersion(RestCountVO vo) {
        return likePostTemplate.execute(status -> {
            if (vo == null) {
                return "数据为空";
            }

            boolean updated = false;
            int maxRetries = 3;
            int retries = 0;

            // 使用前端传递过来的版本号进行更新，如果更新失败则重试
            while (!updated && retries < maxRetries) {
                // 尝试更新
                updated = this.updateUnLikeAndVersionByPostId(vo);

                if (!updated) {
                    // 更新失败，尝试获取最新的版本号并重新更新
                    Post post = this.getLikeAndVersionByPostId(vo.getId());
                    if (post != null) {
                        vo.setVersion(post.getVersion());
                    }
                    /* TODO: Written by - Han Yongding 2024/04/15 重试次数增加 */
                    retries++;
                    try {
                        Thread.sleep(200); // 重试间隔，可以根据需要调整
                    } catch (InterruptedException e) {
                        /* TODO: Written by - Han Yongding 2024/04/15 回滚数据 */
                        status.setRollbackOnly();
                        /* TODO: Written by - Han Yongding 2024/04/15 终止线程 */
                        Thread.currentThread().interrupt();
                    }
                }
            }

            /* TODO: Written by - Han Yongding 2024/04/15 更新失败，扔到队列中让队列处理 */
            if (!updated) {
                likeTemplate.convertAndSend("postUnLikeRetry", vo) ;
                return "取消点赞失败，请稍候重试";
            }

            /* TODO: Written by - Han Yongding 2024/04/15 更新成功 */
            return null;
        });
    }


    /* TODO: Written by - Han Yongding 2024/04/30 注入评论事务 */
    @Resource
    private TransactionTemplate commentPostTemplate;

    /* TODO: Written by - Han Yongding 2024/04/30 发布评论 */
    @Override
    public String commentPost(RestCommentVO vo) {
        return commentPostTemplate.execute(status -> {
            if (vo == null) {
                return null;
            }

            /* TODO: Written by - Han Yongding 2024/04/30 插入评论表 */
            String insertComment = this.insertComment(vo);
            if (insertComment == null) {
                /* TODO: Written by - Han Yongding 2024/04/30 插入失败 */
                return null ;
            }

            /* TODO: Written by - Han Yongding 2024/04/30 更新post表评论数量和版本号 */
            String updateComment = updatePostCommentCountAndVersion(vo.getPostId()) ;
            if (updateComment != null) {
                /* TODO: Written by - Han Yongding 2024/04/30 尝试回滚 */
                status.setRollbackOnly();
                return null;
            }

            /* TODO: Written by - Han Yongding 2024/04/15 成功则同步更新单条数据 */
            amqpTemplate.convertAndSend("postSyncES", vo.getPostId()) ;

            /* TODO: Written by - Han Yongding 2024/04/30 评论发布成功 */
            return insertComment ;
        });
    }

    /* TODO: Written by - Han Yongding 2024/04/30 插入评论表 */
    private String insertComment(RestCommentVO vo) {
        return commentPostTemplate.execute(status -> {

            Comment viewObject = vo.asViewObject(Comment.class);
            String commentId = commentService.addComment(viewObject);
            /* TODO: Written by - Han Yongding 2024/04/30 插入失败或成功 */
            return Objects.requireNonNullElse(commentId, null);

        });
    }

    /* TODO: Written by - Han Yongding 2024/04/30 根据帖子ID查询评论数量和版本号 */
    @Override
    public Post getCommentAndVersionByPostId(String postId) {
        return mapper.getCommentAndVersionByPostId(postId);
    }

    /* TODO: Written by - Han Yongding 2024/04/30 根据版本号和PostId更新帖子评论数量 */
    @Override
    public Boolean updateCommentAndVersionByPostId(Post post) {
        return mapper.updateCommentAndVersionByPostId(post) == 1;
    }

    /* TODO: Written by - Han Yongding 2024/04/30 更新post表评论数量和版本号 */
    private String updatePostCommentCountAndVersion(String postId) {
        Post post = this.getCommentAndVersionByPostId(postId);
        post.setPostId(postId);
        return commentPostTemplate.execute(status -> {
            if (postId == null) {
                return "数据为空";
            }

            boolean updated = false;
            int maxRetries = 3;
            int retries = 0;

            /* TODO: Written by - Han Yongding 2024/04/30 使用前端传递过来的版本号进行更新，如果更新失败则重试 */
            while (!updated && retries < maxRetries) {
                // 尝试更新
                updated = this.updateCommentAndVersionByPostId(post);

                if (!updated) {
                    // 更新失败，尝试获取最新的版本号并重新更新
                    Post post1 = this.getCommentAndVersionByPostId(postId);
                    if (post1 != null) {
                        post.setVersion(post1.getVersion());
                    }
                    /* TODO: Written by - Han Yongding 2024/04/15 重试次数增加 */
                    retries++;
                    try {
                        Thread.sleep(200); // 重试间隔，可以根据需要调整
                    } catch (InterruptedException e) {
                        /* TODO: Written by - Han Yongding 2024/04/15 回滚数据 */
                        status.setRollbackOnly();
                        /* TODO: Written by - Han Yongding 2024/04/15 终止线程 */
                        Thread.currentThread().interrupt();
                    }
                }
            }

            /* TODO: Written by - Han Yongding 2024/04/15 更新失败 */
            if (!updated) {
                /* TODO: Written by - Han Yongding 2024/04/30 回滚 */
                status.setRollbackOnly() ;
                return "评论发布失败，请稍候重试";
            }

            /* TODO: Written by - Han Yongding 2024/04/30 更新成功 */
            return null;
        });
    }

    /* TODO: Written by - Han Yongding 2024/06/16 根据用户ID查询相关的帖子ID */
    @Override
    public List<String> getPostIdByUserId(String userId) {
        return mapper.getPostIdByUserId(userId);
    }
}
