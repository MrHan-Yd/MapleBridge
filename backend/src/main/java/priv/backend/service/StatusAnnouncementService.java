package priv.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import priv.backend.domain.vo.request.RestStatusAnnouncementVO;
import priv.backend.domain.vo.response.RespStatusAnnouncementVO;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 通知公告业务层
 * @CreateDate :  2024-03-04 12:45
 */
public interface StatusAnnouncementService {

    /** TODO: Written by - Han Yongding 2024/03/04 新增公告状态 */
    String insertAnnouncement(RestStatusAnnouncementVO vo) ;

    /** TODO: Written by - Han Yongding 2024/03/04 分页查询所有公告状态 */
    Page<RespStatusAnnouncementVO> getPageStatusAnnouncement(int pageNum, int pageSize) ;

    /** TODO: Written by - Han Yongding 2024/03/04 修改公告状态 */
    String updateStatusAnnouncementByStatusId(RestStatusAnnouncementVO vo) ;

    /** TODO: Written by - Han Yongding 2024/03/04 根据公告状态ID删除对应数据 */
    String deleteStatusAnnouncementByStatusId(String statusId) ;
}
