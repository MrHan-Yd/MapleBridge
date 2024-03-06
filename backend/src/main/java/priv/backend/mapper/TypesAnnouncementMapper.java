package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import priv.backend.domain.dto.TypesAnnouncement;
import priv.backend.domain.vo.response.RespTypesAnnouncementSelectVO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 公告类型DAO
 * @CreateDate :  2024-03-04 17:29
 */
public interface TypesAnnouncementMapper extends BaseMapper<TypesAnnouncement> {

    /** TODO: Written by - Han Yongding 2024/03/04 分页查询所有公告类型 */
    Page<TypesAnnouncement> getPageTypesAnnouncement(Page<TypesAnnouncement> page);

    /** TODO: Written by - Han Yongding 2024/03/04 查询所有公告类型 */
    List<RespTypesAnnouncementSelectVO> getAllTypesAnnouncement();

    /** TODO: Written by - Han Yongding 2024/03/07 根据id查询对应的公告类型 */
    RespTypesAnnouncementSelectVO getTypesAnnouncementById(String typeId) ;
}
