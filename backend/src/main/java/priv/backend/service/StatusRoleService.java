package priv.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import priv.backend.domain.dto.StatusRole;
import priv.backend.domain.vo.request.RestRoleStateVO;
import priv.backend.domain.vo.request.RestStatusRoleVO;
import priv.backend.domain.vo.response.RespStatusRoleVO;
import priv.backend.enumeration.StatusEnum;
import priv.backend.mapper.StatusRoleMapper;
import priv.backend.util.CurrentUtils;
import priv.backend.util.PageUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 角色状态业务层
 * @CreateDate :  2024-01-22 16:22
 */
@Service
public class StatusRoleService {
    /* TODO: Written by - Han Yongding 2024/01/23 用户状态DAO注入 */
    @Resource
    private StatusRoleMapper mapper;

    /* TODO: Written by - Han Yongding 2024/01/22 新增用户状态 */
    public String insertStatusRole(RestStatusRoleVO vo) {
        if (vo == null) {
            return "数据不能为空，请重试";
        }
        StatusRole statusRole = vo.asViewObject(StatusRole.class);
        statusRole.setCreateId("null");
        statusRole.setCreateTime(CurrentUtils.getTheCurrentSystemTime());
        statusRole.setState(StatusEnum.NORMAL.STATE);
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.insert(statusRole))) {
            return "添加失败，请重试";
        }
        return null;
    }

    /* TODO: Written by - Han Yongding 2024/01/23 查询所有角色状态 */
    public Page<RespStatusRoleVO> getAllStatusRoles(int pageNum, int pageSize) {
        Page<StatusRole> page = new Page<>(pageNum, pageSize);
        Page<StatusRole> statusRolePage = mapper.getAllStatusRoles(page);

        /* TODO: Written by - Han Yongding 2024/01/23 从数据库中查询后转为响应对象返回 */
        List<RespStatusRoleVO> list = statusRolePage
                .getRecords()
                .stream()
                .map(p -> p.asViewObject(RespStatusRoleVO.class))
                .toList();

        return PageUtils.convertToPage(statusRolePage, list);
    }

    /* TODO: Written by - Han Yongding 2024/01/24 根据ID修改角色状态 */
    public String updateStatusRoleStateById(RestRoleStateVO vo) {

        if (vo == null) {
            return "数据错误，请重试" ;
        }

        if(mapper.updateById(vo.asViewObject(StatusRole.class)) < 1) {
            return "修改失败，请稍后重试" ;
        }
        return null ;
    }


}
