package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import priv.backend.domain.dto.Announcement;
import priv.backend.domain.dto.AnnouncementDto;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 公告DAO
 * @CreateDate :  2024-03-04 20:47
 */
public interface AnnouncementMapper extends BaseMapper<Announcement> {

    /** TODO: Written by - Han Yongding 2024/03/04 分页查询公告 */
    Page<AnnouncementDto> getPageAnnouncement(Page<AnnouncementDto> page);
}
