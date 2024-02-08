package priv.backend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import priv.backend.domain.dto.Permission;
import priv.backend.domain.vo.request.RestPermissionVO;
import priv.backend.domain.vo.response.RespPermissionVO;
import priv.backend.mapper.PermissionMapper;
import priv.backend.service.PermissionService;
import priv.backend.util.CurrentUtils;
import priv.backend.util.PageUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 权限业务层实现类
 * @CreateDate :  2024-01-27 12:56
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    /* TODO: Written by - Han Yongding 2024/01/27 注入权限表DAO层 */
    @Resource
    private PermissionMapper mapper ;

    /* TODO: Written by - Han Yongding 2024/01/27 查询权限表 */
    @Override
    public Object getAllPermission(int pageNum, int pageSize, boolean isItPaginated) {
        if (isItPaginated) {
            Page<Permission> page = new Page<>(pageNum, pageSize) ;
            Page<Permission> returnPage = mapper.getPagePermission(page) ;

            /* TODO: Written by - Han Yongding 2024/01/27 拷贝属性 */
            List<RespPermissionVO> list = returnPage
                    .getRecords()
                    .stream()
                    .map(p -> p.asViewObject(RespPermissionVO.class))
                    .toList() ;

            return PageUtils.convertToPage(returnPage, list) ;
        } else {
            List<RespPermissionVO> list = mapper.getAllPermission().stream().map(p -> p.asViewObject(RespPermissionVO.class)).toList();
            return list ;
        }

    }

    /* TODO: Written by - Han Yongding 2024/01/27 新增权限 */
    @Override
    public String insertPermission(RestPermissionVO vo) {
        if (vo == null) {
            return "数据不能为空，请稍后重试" ;
        }

        /* TODO: Written by - Han Yongding 2024/01/27 拷贝对象属性 */
        Permission permission = vo.asViewObject(Permission.class) ;
        /* TODO: Written by - Han Yongding 2024/01/27 初始化 */
        permission.setCreateId("null");
        /* TODO: Written by - Han Yongding 2024/01/27 获取系统当前时间 */
        permission.setCreateTime(CurrentUtils.getTheCurrentSystemTime()) ;
        /* TODO: Written by - Han Yongding 2024/01/27 状态为在用 */
        permission.setStatusId("1750807828107321346") ;

        /* TODO: Written by - Han Yongding 2024/01/27 新增 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.insert(permission))) {
            return "添加失败，请稍后重试" ;
        }

        /* TODO: Written by - Han Yongding 2024/01/27 添加成功 */
        return null ;
    }

    /* TODO: Written by - Han Yongding 2024/01/27 修改权限 */
    @Override
    public String updatePermission(RestPermissionVO vo) {
        if (vo == null) {
            return "数据不能为空，请稍后重试" ;
        }

        /* TODO: Written by - Han Yongding 2024/01/27 根据id修改 */
        if (mapper.updateById(vo.asViewObject(Permission.class)) < 1) {
            return "修改失败，请稍后重试" ;
        }
        return null ;
    }

    /* TODO: Written by - Han Yongding 2024/01/27 删除权限(伪删除) */
    @Override
    public String deletePermission(String permissionId) {
        if (permissionId == null) {
            return "数据为空，请稍后重试" ;
        }

        /* TODO: Written by - Han Yongding 2024/01/28 伪删除状态 */
        Permission vo = new Permission() ;
        vo.setPermissionId(permissionId) ;
        vo.setStatusId("1750807870989885442") ;
        /* TODO: Written by - Han Yongding 2024/01/27 根据id修改为删除标识 */
        if (mapper.updateById(vo) < 1) {
            return "删除失败，请稍后重试" ;
        }
        return null ;
    }
}
