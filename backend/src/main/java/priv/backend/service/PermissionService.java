package priv.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import priv.backend.domain.dto.Permission;
import priv.backend.domain.vo.request.RestPermissionVO;
import priv.backend.domain.vo.response.RespPermissionVO;
import priv.backend.mapper.PermissionMapper;
import priv.backend.util.CurrentUtils;
import priv.backend.util.PageUtils;
import priv.backend.util.ReturnUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 权限业务层
 * @CreateDate :  2024-01-27 12:56
 */
@Service
public class PermissionService {
    /* TODO: Written by - Han Yongding 2024/01/27 注入权限表DAO层 */
    @Resource
    private PermissionMapper mapper ;

    /* TODO: Written by - Han Yongding 2024/01/27 查询权限表 */
    public Page<RespPermissionVO> getAllPermission(int pageNum, int pageSize) {
        Page<Permission> page = new Page<>(pageNum, pageSize) ;
        Page<Permission> returnPage = mapper.getAllPermission(page) ;

        /* TODO: Written by - Han Yongding 2024/01/27 拷贝属性 */
        List<RespPermissionVO> list = returnPage
                .getRecords()
                .stream()
                .map(p -> p.asViewObject(RespPermissionVO.class))
                .toList() ;

        return PageUtils.convertToPage(returnPage, list) ;
    }

    /* TODO: Written by - Han Yongding 2024/01/27 新增权限 */
    public String addPermission(RestPermissionVO vo) {
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
}
