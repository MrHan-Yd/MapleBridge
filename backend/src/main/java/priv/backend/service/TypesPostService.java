package priv.backend.service;

import priv.backend.domain.PageBean;
import priv.backend.domain.RestBean;
import priv.backend.domain.vo.request.RestTypesAnnouncementVO;
import priv.backend.domain.vo.request.RestTypesPostVO;
import priv.backend.domain.vo.response.RespTypesPostSelectVO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 帖子类型业务层
 * @CreateDate :  2024-03-08 12:40
 */
public interface TypesPostService {
    /* TODO: Written by - Han Yongding 2024/03/08 查询帖子类型，分页或不分页 */
    Object getPostTypes(PageBean pageBean);

    /* TODO: Written by - Han Yongding 2024/03/08 查询帖子类型，不分页 */
    Object getPostTypes();

    /** TODO: Written by - Han Yongding 2024/03/08 新增帖子类型 */
    String insertTypesPost(RestTypesPostVO vo);

    /** TODO: Written by - Han Yongding 2024/03/08 修改帖子类型 */
    String updateTypesPostByTypeId(RestTypesPostVO vo);

    /** TODO: Written by - Han Yongding 2024/03/08 删除帖子类型 */
    String deleteTypesPostByTypeId(String typeId) ;
}
