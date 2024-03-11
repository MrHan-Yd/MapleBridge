package priv.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import priv.backend.domain.PageBean;
import priv.backend.domain.vo.request.RestPostVO;
import priv.backend.domain.vo.response.RespPostVO;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 帖子业务层
 * @CreateDate :  2024-03-08 15:18
 */
public interface PostService {

    /** TODO: Written by - Han Yongding 2024/03/09 分页查询所有帖子 */
    Page<RespPostVO> getPagePost(PageBean pageBean);

    /** TODO: Written by - Han Yongding 2024/03/09 新增帖子 */
    String insertPost(RestPostVO vo);

    /** TODO: Written by - Han Yongding 2024/03/10 修改帖子 */
    String updatePost(RestPostVO vo);

    /** TODO: Written by - Han Yongding 2024/03/10 删除帖子 */
    String deletePost(String postId);
}
