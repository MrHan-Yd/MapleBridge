package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import priv.backend.domain.dto.StatusAnnouncement;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 公告状态表DAO
 * @CreateDate :  2024-03-04 12:43
 */

public interface StatusAnnouncementMapper extends BaseMapper<StatusAnnouncement> {
    /** TODO: Written by - Han Yongding 2024/03/04 分页查询所有公告状态 */
    Page<StatusAnnouncement> getPageStatusAnnouncement(Page<StatusAnnouncement> page) ;
}
