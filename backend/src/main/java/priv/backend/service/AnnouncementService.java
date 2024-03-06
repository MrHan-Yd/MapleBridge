package priv.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import priv.backend.domain.vo.request.RestAnnouncementVO;
import priv.backend.domain.vo.response.RespAnnouncementVO;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 公告业务层
 * @CreateDate :  2024-03-04 20:47
 */
public interface AnnouncementService {

    /** TODO: Written by - Han Yongding 2024/03/04 分页查询所有公告 */
    Page<RespAnnouncementVO> getPageAnnouncement(int pageNum, int pageSize);

    /** TODO: Written by - Han Yongding 2024/03/04 插入公告 */
    String insertAnnouncement(RestAnnouncementVO vo) ;

    /** TODO: Written by - Han Yongding 2024/03/04 修改公告 */
    String updateAnnouncementById(RestAnnouncementVO vo);

    /** TODO: Written by - Han Yongding 2024/03/04 删除公告 */
    String deleteAnnouncementById(String id) ;
}
