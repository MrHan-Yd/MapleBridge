package priv.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import priv.backend.domain.RestBean;
import priv.backend.domain.vo.request.*;
import priv.backend.domain.vo.response.RespRoleVO;
import priv.backend.service.impl.*;
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
    private StatusRoleServiceImpl statusRoleService;


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
    private StatusPermissionServiceImpl statusPermissionService ;

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
    private PermissionServiceImpl permissionService ;

    /* TODO: Written by - Han Yongding 2024/01/27 查询所有权限数据，后续再优化 */
    @GetMapping("permission")
    public RestBean<Object> getAllPermission(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "true") boolean isItPaginated) {
        return ReturnUtils
                .messageHandleData(() -> permissionService.getAllPermission(pageNum, pageSize, isItPaginated)) ;
    }

    /* TODO: Written by - Han Yongding 2024/01/27 新增权限 */
    @PostMapping("permission")
    public RestBean<Void> addPermission(@RequestBody RestPermissionVO vo) {
        return ReturnUtils
                .messageHandle(vo, permissionService::insertPermission) ;
    }

    /* TODO: Written by - Han Yongding 2024/01/27 修改权限 */
    @PutMapping("permission")
    public RestBean<Void> updatePermission(@RequestBody RestPermissionVO vo) {
        return ReturnUtils
                .messageHandle(vo, permissionService::updatePermission) ;
    }

    /* TODO: Written by - Han Yongding 2024/01/27 删除权限 */
    @DeleteMapping("permission/{permissionId}")
    public RestBean<Void> deletePermission(@PathVariable("permissionId") String permissionId) {
        return ReturnUtils
                .messageHandle(() -> permissionService.deletePermission(permissionId)) ;
    }

    /* TODO: Written by - Han Yongding 2024/01/29 注入角色业务层 */
    @Resource
    private RoleServiceImpl roleService ;

    /* TODO: Written by - Han Yongding 2024/01/31 查询所有角色和相应权限 */
    @GetMapping("role")
    public RestBean<Page<RespRoleVO>> getRole(@RequestParam(defaultValue = "1") int pageNum,
                                              @RequestParam(defaultValue = "10") int pageSize) {
        return ReturnUtils.messageHandleData(() -> roleService.getRoles(pageNum, pageSize)) ;
    }

    /* TODO: Written by - Han Yongding 2024/01/29 新增角色权限 */
    @PostMapping("role")
    public RestBean<Void> addRole(@RequestBody RestRoleVO vo) {
        return ReturnUtils.messageHandle(vo,roleService::insertRole) ;
    }

    /* TODO: Written by - Han Yongding 2024/01/31 修改角色状态 */
    @PutMapping("role")
    public RestBean<Void> updateRole(@RequestBody RestRoleVO vo) {
        System.out.println(vo) ;
//        return ReturnUtils
//                .messageHandle(vo, roleService::updateRole) ;
        return null ;
    }

    /* TODO: Written by - Han Yongding 2024/01/27 删除角色 */
    @DeleteMapping("role/{roleId}")
    public RestBean<Void> deleteRole(@PathVariable("roleId") String roleId) {
        System.out.println(roleId);
//        return ReturnUtils
//                .messageHandle(() -> permissionService.deletePermission(permissionId)) ;
        return null;
    }

    /* TODO: Written by - Han Yongding 2024/02/06 注入用户等级表业务层 */
    @Resource
    private UserLevelServiceImpl userLevelService ;

    /* TODO: Written by - Han Yongding 2024/02/06 分页查询用户等级表所有数据 */
    @GetMapping("user-level")
    public RestBean<Object> getAllUserLevel(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ReturnUtils.messageHandleData(() -> userLevelService.getAllUserLevel(pageNum, pageSize)) ;
    }

    /* TODO: Written by - Han Yongding 2024/02/06 新增用户等级 */
    @PostMapping("user-level")
    public RestBean<Void> insertUserLevel(@RequestBody RestUserLevelVO vo) {
        return ReturnUtils.messageHandle(vo, userLevelService::insertUserLevel) ;
    }

    /* TODO: Written by - Han Yongding 2024/02/06 修改用户状态 */
    @PutMapping("user-level")
    public RestBean<Void> updateUserLevel(@RequestBody RestUserLevelVO vo) {
        return ReturnUtils.messageHandle(vo, userLevelService::updateUserLevel) ;
    }

    /* TODO: Written by - Han Yongding 2024/02/06 根据等级ID删除用户等级 */
    @DeleteMapping("user-level/{levelId}")
    public RestBean<Void> deleteUserLevel(@PathVariable("levelId") String levelId) {
        return ReturnUtils.messageHandle(() -> userLevelService.deleteUserLevel(levelId)) ;
    }

    /* TODO: Written by - Han Yongding 2024/02/08 注入用户状态业务层 */
    @Resource
    private StatusUserServiceImpl statusUserService ;

    /* TODO: Written by - Han Yongding 2024/02/06 分页查询用户状态表所有数据 */
    @GetMapping("user-status")
    public RestBean<Object> getAllStatusUser(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ReturnUtils.messageHandleData(() -> statusUserService.getAllStatusUsers(pageNum, pageSize)) ;
    }

    /* TODO: Written by - Han Yongding 2024/02/06 新增用户状态 */
    @PostMapping("user-status")
    public RestBean<Void> insertStatusUser(@RequestBody RestStatusUserVO vo) {
        return ReturnUtils.messageHandle(vo, statusUserService::insertStatusUser) ;
    }

    /* TODO: Written by - Han Yongding 2024/02/06 修改用户状态 */
    @PutMapping("user-status")
    public RestBean<Void> updateStatusUser(@RequestBody RestUserStateVO vo) {
        return ReturnUtils.messageHandle(vo, statusUserService::updateStatusUserStateById) ;
    }

    /* TODO: Written by - Han Yongding 2024/02/06 根据等级ID删除用户等级 */
    @DeleteMapping("user-status/{statusId}")
    public RestBean<Void> deleteStatusUser(@PathVariable("statusId") String statusId) {
        return ReturnUtils.messageHandle(() -> statusUserService.deleteStatusUserByStatusId(statusId)) ;
    }

}
