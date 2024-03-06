package priv.backend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import priv.backend.domain.dto.StatusAnnouncement;
import priv.backend.domain.vo.request.RestStatusAnnouncementVO;
import priv.backend.domain.vo.response.RespStatusAnnouncementVO;
import priv.backend.enumeration.StatusEnum;
import priv.backend.mapper.StatusAnnouncementMapper;
import priv.backend.service.StatusAnnouncementService;
import priv.backend.util.CurrentUtils;
import priv.backend.util.PageUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 通知公告业务层实现类
 * @CreateDate :  2024-03-04 12:46
 */
@Service
public class StatusAnnouncementServiceImpl implements StatusAnnouncementService {

    /* TODO: Written by - Han Yongding 2024/03/04 注入公告状态表DAO */
    @Resource
    private StatusAnnouncementMapper mapper ;

    /** TODO: Written by - Han Yongding 2024/03/04 新增公告状态 */
    @Override
    public String insertAnnouncement(RestStatusAnnouncementVO vo) {
        /* TODO: Written by - Han Yongding 2024/03/04 vo为空 */
        if (vo == null) {
            return "数据为空，请稍候再试" ;
        }

        /* TODO: Written by - Han Yongding 2024/03/04 拷贝对象 */
        StatusAnnouncement statusAnnouncement = vo.asViewObject(StatusAnnouncement.class) ;
        /* TODO: Written by - Han Yongding 2024/03/04 初始化 */
        statusAnnouncement.setState(StatusEnum.NORMAL.STATE) ;
        statusAnnouncement.setCreateTime(CurrentUtils.getTheCurrentSystemTime());

        /* TODO: Written by - Han Yongding 2024/03/04 新增失败 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.insert(statusAnnouncement))) {
            return "新增失败，请稍候再试" ;
        }

        /* TODO: Written by - Han Yongding 2024/03/04 新增成功 */
        return null ;
    }

    /** TODO: Written by - Han Yongding 2024/03/04 分页查询所有公告状态 */
    @Override
    public Page<RespStatusAnnouncementVO> getPageStatusAnnouncement(int pageNum, int pageSize) {
        Page<StatusAnnouncement> page = new Page<>(pageNum, pageSize) ;
        Page<StatusAnnouncement> respPage = mapper.getPageStatusAnnouncement(page) ;

        /* 拷贝属性 */
        List<RespStatusAnnouncementVO> list = respPage.
                getRecords()
                .stream()
                .map(p -> p.asViewObject(RespStatusAnnouncementVO.class))
                .toList() ;
        return PageUtils.convertToPage(respPage, list);
    }

    /** TODO: Written by - Han Yongding 2024/03/04 修改公告状态 */
    @Override
    public String updateStatusAnnouncementByStatusId(RestStatusAnnouncementVO vo) {

        /* TODO: Written by - Han Yongding 2024/03/04 数据为空 */
        if (vo == null) {
            return "数据为空，请稍候再试" ;
        }

        StatusAnnouncement viewObject = vo.asViewObject(StatusAnnouncement.class);
        /* TODO: Written by - Han Yongding 2024/03/05 修改时间 */
        viewObject.setUpdateTime(CurrentUtils.getTheCurrentSystemTime());

        /* TODO: Written by - Han Yongding 2024/03/04 修改失败 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.updateById(viewObject))) {
            return "修改失败，请稍候再试" ;
        }

        /* TODO: Written by - Han Yongding 2024/03/04 修改成功 */
        return null;
    }

    /** TODO: Written by - Han Yongding 2024/03/04 根据公告状态ID删除对应数据 */

    @Override
    public String deleteStatusAnnouncementByStatusId(String statusId) {
        if (statusId == null) {
            return "唯一标识为空，请稍后再试" ;
        }

        StatusAnnouncement statusAnnouncement = new StatusAnnouncement();
        statusAnnouncement.setStatusId(statusId) ;

        /* TODO: Written by - Han Yongding 2024/03/04 删除失败 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.deleteById(statusAnnouncement))) {
            return "删除失败，请稍后再试" ;
        }

        /* TODO: Written by - Han Yongding 2024/03/04 删除成功 */
        return null;
    }
}
