package priv.backend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import priv.backend.domain.dto.Announcement;
import priv.backend.domain.dto.AnnouncementDto;
import priv.backend.domain.vo.request.RestAnnouncementVO;
import priv.backend.domain.vo.response.RespAnnouncementVO;
import priv.backend.mapper.AnnouncementMapper;
import priv.backend.service.AnnouncementService;
import priv.backend.util.CurrentUtils;
import priv.backend.util.PageUtils;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 公告业务层实现类
 * @CreateDate :  2024-03-04 20:48
 */
@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    /* TODO: Written by - Han Yongding 2024/03/04 注入用户公告DAO */
    @Resource
    private AnnouncementMapper mapper;

    /** TODO: Written by - Han Yongding 2024/03/04 分页查询所有公告 */
    @Override
    public Page<RespAnnouncementVO> getPageAnnouncement(int pageNum, int pageSize) {
        Page<AnnouncementDto> page = new Page<>(pageNum, pageSize);
        Page<AnnouncementDto> resp = mapper.getPageAnnouncement(page) ;

        /* TODO: Written by - Han Yongding 2024/03/04 拷贝属性 */
        List<RespAnnouncementVO> list = resp
                .getRecords()
                .stream()
                .map(p -> {
                    RespAnnouncementVO viewObject = p.asViewObject(RespAnnouncementVO.class);
                    Timestamp[] releaseAndDeadline = new Timestamp[2];
                    releaseAndDeadline[0] = p.getPublishTime() ;
                    releaseAndDeadline[1] = p.getDeadline() ;
                    viewObject.setReleaseAndDeadline(releaseAndDeadline);
                    return viewObject ;
                })
                .toList() ;

        return PageUtils.convertToPage(page, list) ;
    }

    /** TODO: Written by - Han Yongding 2024/03/04 插入公告 */
    @Override
    public String insertAnnouncement(RestAnnouncementVO vo) {
        if (vo == null) {
            return "数据为空，请稍后再试" ;
        }
        /* TODO: Written by - Han Yongding 2024/03/05 拷贝属性 */
        Announcement viewObject = vo.asViewObject(Announcement.class);
        viewObject.setPublishTime(vo.getReleaseAndDeadline()[0]);
        viewObject.setDeadline(vo.getReleaseAndDeadline()[1]);
        viewObject.setStatusId("1764526473467625473") ;
        viewObject.setCreateTime(CurrentUtils.getTheCurrentSystemTime()) ;

        /* TODO: Written by - Han Yongding 2024/03/04 插入失败 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.insert(viewObject))) {
            return "新增失败，请稍后再试" ;
        }

        /* TODO: Written by - Han Yongding 2024/03/04 新增成功 */
        return null;
    }

    /** TODO: Written by - Han Yongding 2024/03/04 修改公告 */
    @Override
    public String updateAnnouncementById(RestAnnouncementVO vo) {
        if (vo == null) {
            return "数据为空，请稍后再试" ;
        }
        /* TODO: Written by - Han Yongding 2024/03/06 拷贝属性 */
        Announcement viewObject = vo.asViewObject(Announcement.class);
        /* TODO: Written by - Han Yongding 2024/03/06 如果只是开启或禁用状态，不赋值开始时间和结束时间字段(字段为空) */
        if (vo.getReleaseAndDeadline() != null) {
            viewObject.setPublishTime(vo.getReleaseAndDeadline()[0]);
            viewObject.setDeadline(vo.getReleaseAndDeadline()[1]);
        }
        /* TODO: Written by - Han Yongding 2024/03/06 修改时间 */
        viewObject.setUpdateTime(CurrentUtils.getTheCurrentSystemTime()) ;

        /* TODO: Written by - Han Yongding 2024/03/04 修改失败 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.updateById(viewObject))) {
            return "修改失败，请稍后再试" ;
        }

        /* TODO: Written by - Han Yongding 2024/03/04 修改成功 */
        return null;
    }

    /** TODO: Written by - Han Yongding 2024/03/04 删除公告 */
    @Override
    public String deleteAnnouncementById(String id) {
        if (id == null) {
            return "唯一标识为空，请稍后重试" ;
        }

        /* TODO: Written by - Han Yongding 2024/03/04 删除失败，伪删除 */
        Announcement viewObject = new Announcement();
        viewObject.setId(id);
        viewObject.setStatusId("1764526553247481858") ;
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.updateById(viewObject))) {
            return "删除失败，请稍后重试" ;
        }

        /* TODO: Written by - Han Yongding 2024/03/04 删除成功 */
        return null;
    }
}
