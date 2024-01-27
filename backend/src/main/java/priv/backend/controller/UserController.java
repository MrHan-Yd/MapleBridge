package priv.backend.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import priv.backend.domain.RestBean;
import priv.backend.domain.vo.request.RestPermissionStateVO;
import priv.backend.domain.vo.request.RestRoleStateVO;
import priv.backend.domain.vo.request.RestStatusPermissionVO;
import priv.backend.domain.vo.request.RestStatusRoleVO;
import priv.backend.service.PermissionService;
import priv.backend.service.StatusPermissionService;
import priv.backend.service.StatusRoleService;
import priv.backend.util.ReturnUtils;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 处理用户相关控制器
 * @CreateDate :  2024-01-22 16:02
 */
@RestController
@RequestMapping("/api/auth/")
public class UserController {

    /* TODO: Written by - Han Yongding 2024/01/23 注入角色状态业务层 */
    @Resource
    private StatusRoleService statusRoleService;


    /* TODO: Written by - Han Yongding 2024/01/23 用户状态新增 */
    @PostMapping("role-status")
    public RestBean<Void> addStatusRole(@RequestBody RestStatusRoleVO vo) {
        return ReturnUtils
                .messageHandle(vo, statusRoleService::insertStatusRole);
    }

    /* TODO: Written by - Han Yongding 2024/01/23 获取所有角色状态 */
    @GetMapping("role-status")
    public RestBean<Object> getAllStatusRoles(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ReturnUtils
                .messageHandleData(
                        () -> statusRoleService.getAllStatusRoles(pageNum, pageSize));
    }

//    /* TODO: Written by - Han Yongding 2024/01/23 根据ID查询特定状态 */
//    @GetMapping("/role-status/{id}")
//    public RestBean<RespStatusRoleVO> getStatusRoleById(@PathVariable String id) {
//        // 实现根据ID查询对象的逻辑
//        // 使用id参数来获取特定对象
//    }

    /* TODO: Written by - Han Yongding 2024/01/26 更新角色状态信息 */
    @PutMapping("role-status")
    public RestBean<Void> updateStatusRoleState(@RequestBody RestRoleStateVO vo) {
        return ReturnUtils.messageHandle(vo, statusRoleService::updateStatusRoleStateById) ;
    }

    /* TODO: Written by - Han Yongding 2024/01/26 注入权限状态业务层 */
    @Resource
    private StatusPermissionService statusPermissionService ;

    /* TODO: Written by - Han Yongding 2024/01/23 用户状态新增 */
    @PostMapping("permission-status")
    public RestBean<Void> addStatusPermission(@RequestBody RestStatusPermissionVO vo) {
        return ReturnUtils
                .messageHandle(vo, statusPermissionService::insertStatusPermission);
    }

    /* TODO: Written by - Han Yongding 2024/01/23 获取所有角色状态 */
    @GetMapping("permission-status")
    public RestBean<Object> getAllStatusPermission(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ReturnUtils
                .messageHandleData(
                        () -> statusPermissionService.getAllStatusRoles(pageNum, pageSize));
    }

    /* TODO: Written by - Han Yongding 2024/01/26 更新角色状态信息 */
    @PutMapping("permission-status")
    public RestBean<Void> updatePermissionState(@RequestBody RestPermissionStateVO vo) {
        return ReturnUtils
                .messageHandle(vo, statusPermissionService::updatePermissionStateById) ;
    }

    /* TODO: Written by - Han Yongding 2024/01/27 注入权限业务层 */
    @Resource
    private PermissionService permissionService ;

    /* TODO: Written by - Han Yongding 2024/01/27 查询所有权限数据，后续再优化 */
    @GetMapping("permission")
    public RestBean<Object> getAllPermission(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ReturnUtils
                .messageHandleData(() -> permissionService.getAllPermission(pageNum, pageSize)) ;
    }
}
