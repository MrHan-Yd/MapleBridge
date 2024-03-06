package priv.backend.service;

import priv.backend.domain.vo.request.RestTypesAnnouncementVO;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 公告类型业务层
 * @CreateDate :  2024-03-04 17:30
 */
public interface TypesAnnouncementService {

    /** TODO: Written by - Han Yongding 2024/03/04 查询公告类型，分页或不分页 */
    Object getAnnouncementTypes(int pageNum, int pageSize, boolean isItPaginated);

    /** TODO: Written by - Han Yongding 2024/03/04 新增公告类型 */
    String insertTypesAnnouncement(RestTypesAnnouncementVO vo);

    /** TODO: Written by - Han Yongding 2024/03/04 修改公告类型 */
    String updateTypesAnnouncementByTypeId(RestTypesAnnouncementVO vo);

    /** TODO: Written by - Han Yongding 2024/03/04 删除公告类型 */
    String deleteTypesAnnouncementByTypeId(String typeId) ;
}
