package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import priv.backend.domain.dto.TypesPost;
import priv.backend.domain.vo.response.RespTypesAnnouncementSelectVO;
import priv.backend.domain.vo.response.RespTypesPostSelectVO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 帖子类型DAO
 * @CreateDate :  2024-03-08 12:38
 */
public interface TypesPostMapper extends BaseMapper<TypesPost> {

    /** TODO: Written by - Han Yongding 2024/03/08 分页查询所有帖子类型 */
    Page<TypesPost> getPageTypesPost(Page<TypesPost> page);

    /** TODO: Written by - Han Yongding 2024/03/08 查询所有公告类型 */
    List<RespTypesPostSelectVO> getAllTypesPost();

    /** TODO: Written by - Han Yongding 2024/03/08 根据id查询对应的帖子类型 */
    RespTypesPostSelectVO getTypesPostById(String typeId) ;
}
