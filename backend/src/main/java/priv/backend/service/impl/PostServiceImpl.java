package priv.backend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import priv.backend.domain.PageBean;
import priv.backend.domain.dto.Post;
import priv.backend.domain.vo.request.RestPostVO;
import priv.backend.domain.vo.response.RespPostVO;
import priv.backend.mapper.PostMapper;
import priv.backend.service.PostService;
import priv.backend.util.CurrentUtils;
import priv.backend.util.PageUtils;

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
}
