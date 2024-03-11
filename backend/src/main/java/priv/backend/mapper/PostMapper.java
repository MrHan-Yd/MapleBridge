package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import priv.backend.domain.dto.Post;
import priv.backend.domain.vo.response.RespPostVO;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 帖子DAO
 * @CreateDate :  2024-03-08 15:12
 */
public interface PostMapper extends BaseMapper<Post> {

    /* TODO: Written by - Han Yongding 2024/03/09 分页查询所有帖子 */
    Page<RespPostVO> getPagePost(Page<RespPostVO> page) ;
}