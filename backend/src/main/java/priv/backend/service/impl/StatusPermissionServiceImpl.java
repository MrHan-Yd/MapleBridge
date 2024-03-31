package priv.backend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import priv.backend.domain.dto.StatusPermission;
import priv.backend.domain.vo.request.RestPermissionStateVO;
import priv.backend.domain.vo.request.RestStatusPermissionVO;
import priv.backend.domain.vo.response.RespStatusPermissionVO;
import priv.backend.enumeration.StatusEnum;
import priv.backend.mapper.StatusPermissionMapper;
import priv.backend.service.StatusPermissionService;
import priv.backend.util.CurrentUtils;
import priv.backend.util.PageUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 权限状态业务层实现类
 * @CreateDate :  2024-01-26 16:10
 */
@Service
public class StatusPermissionServiceImpl implements StatusPermissionService {
    /* TODO: Written by - Han Yongding 2024/03/04 注入权限状态DAO */
    @Resource
    private StatusPermissionMapper mapper ;

    /** TODO: Written by - Han Yongding 2024/01/22 新增权限状态 */
    @Override
    public String insertStatusPermission(RestStatusPermissionVO vo) {
        if (vo == null) {
            return "数据不能为空，请重试";
        }
        StatusPermission statusPermission = vo.asViewObject(StatusPermission.class);
        statusPermission.setCreateTime(CurrentUtils.getTheCurrentSystemTime());
        statusPermission.setState(StatusEnum.NORMAL.STATE);
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.insert(statusPermission))) {
            return "添加失败，请重试";
        }
        return null;
    }

    /** TODO: Written by - Han Yongding 2024/01/26 查询所有权限状态 */
    @Override
    public Page<RespStatusPermissionVO> getAllStatusRoles(int pageNum, int pageSize) {
        Page<StatusPermission> page = new Page<>(pageNum, pageSize);
        Page<StatusPermission> statusPermissionPage = mapper.getAllStatusPermission(page);

        /* TODO: Written by - Han Yongding 2024/01/23 从数据库中查询后转为响应对象返回 */
        List<RespStatusPermissionVO> list = statusPermissionPage
                .getRecords()
                .stream()
                .map(p -> p.asViewObject(RespStatusPermissionVO.class))
                .toList();

        return PageUtils.convertToPage(statusPermissionPage, list);
    }

    /** TODO: Written by - Han Yongding 2024/01/26 根据ID修改权限状态 */
    @Override
    public String updatePermissionStateById(RestPermissionStateVO vo) {

        if (vo == null) {
            return "数据错误，请重试" ;
        }
        /* TODO: Written by - Han Yongding 2024/03/30 修改时间 */
        StatusPermission viewObject = vo.asViewObject(StatusPermission.class);
        viewObject.setUpdateTime(CurrentUtils.getTheCurrentSystemTime());

        if(mapper.updateById(viewObject) < 1) {
            return "修改失败，请稍后重试" ;
        }
        return null ;
    }
}
