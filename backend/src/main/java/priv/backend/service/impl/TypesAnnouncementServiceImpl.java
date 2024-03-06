package priv.backend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import priv.backend.domain.dto.TypesAnnouncement;
import priv.backend.domain.vo.request.RestTypesAnnouncementVO;
import priv.backend.domain.vo.response.RespTypesAnnouncementVO;
import priv.backend.mapper.TypesAnnouncementMapper;
import priv.backend.service.TypesAnnouncementService;
import priv.backend.util.CurrentUtils;
import priv.backend.util.PageUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 公告类型实现类
 * @CreateDate :  2024-03-04 17:31
 */
@Service
public class TypesAnnouncementServiceImpl implements TypesAnnouncementService {
    /* TODO: Written by - Han Yongding 2024/03/04 注入公告类型DAO */
    @Resource
    private TypesAnnouncementMapper mapper ;

    /** TODO: Written by - Han Yongding 2024/03/04 查询公告类型，分页或不分页 */
    @Override
    public Object getAnnouncementTypes(int pageNum, int pageSize, boolean isItPaginated) {
        /* TODO: Written by - Han Yongding 2024/03/04 分页 */
        if (isItPaginated) {
            /* TODO: Written by - Han Yongding 2024/03/04 配置分页 */
            Page<TypesAnnouncement> page = new Page<>(pageNum, pageSize);
            /* TODO: Written by - Han Yongding 2024/03/04 获取数据 */
            Page<TypesAnnouncement> resp = mapper.getPageTypesAnnouncement(page);

            /* TODO: Written by - Han Yongding 2024/03/04 拷贝属性 */
            List<RespTypesAnnouncementVO> list = resp
                    .getRecords()
                    .stream()
                    .map(p -> p.asViewObject(RespTypesAnnouncementVO.class))
                    .toList();

            return PageUtils.convertToPage(resp, list) ;
        }
        /* TODO: Written by - Han Yongding 2024/03/04 不分页 */
        return mapper.getAllTypesAnnouncement() ;
    }

    /** TODO: Written by - Han Yongding 2024/03/04 新增公告类型 */
    @Override
    public String insertTypesAnnouncement(RestTypesAnnouncementVO vo) {
        if (vo == null) {
            return "数据为空，请稍后再试" ;
        }

        /* TODO: Written by - Han Yongding 2024/03/04 初始化 */
        TypesAnnouncement typesAnnouncement = vo.asViewObject(TypesAnnouncement.class);
        typesAnnouncement.setCreateTime(CurrentUtils.getTheCurrentSystemTime());

        /* TODO: Written by - Han Yongding 2024/03/04 新增失败 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.insert(typesAnnouncement))) {
            return "新增失败，请稍后再试" ;
        }

        /* TODO: Written by - Han Yongding 2024/03/04 新增成功 */
        return null;
    }

    /* TODO: Written by - Han Yongding 2024/03/04 修改公告类型 */
    @Override
    public String updateTypesAnnouncementByTypeId(RestTypesAnnouncementVO vo) {
        if (vo == null) {
            return "数据为空，请稍后再试" ;
        }

        TypesAnnouncement typesAnnouncement = vo.asViewObject(TypesAnnouncement.class) ;
        typesAnnouncement.setUpdateTime(CurrentUtils.getTheCurrentSystemTime()) ;
        /* TODO: Written by - Han Yongding 2024/03/04 修改失败 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.updateById(typesAnnouncement))) {
            return "修改失败，请稍候再试" ;
        }

        /* TODO: Written by - Han Yongding 2024/03/04 修改成功 */
        return null;
    }

    /* TODO: Written by - Han Yongding 2024/03/04 删除公告类型 */

    @Override
    public String deleteTypesAnnouncementByTypeId(String typeId) {

        if (typeId == null) {
            return "唯一标识为空，请稍候再试" ;
        }

        /* TODO: Written by - Han Yongding 2024/03/04 删除失败 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.deleteById(typeId))) {
            return "删除失败，请稍后再试" ;
        }

        /* TODO: Written by - Han Yongding 2024/03/04 删除成功 */
        return null;
    }
}
