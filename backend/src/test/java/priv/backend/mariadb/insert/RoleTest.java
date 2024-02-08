package priv.backend.mariadb.insert;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import priv.backend.domain.dto.Role;
import priv.backend.service.impl.RoleServiceImpl;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 角色表
 * @CreateDate :  2024-01-31 9:30
 */
@SpringBootTest
public class RoleTest {
    @Resource
    private RoleServiceImpl service;
    @Test
    public void insertRole() {
        Role role = new Role() ;
        role.setRoleName("123");
        role.setRoleNameCn("测试");
        String s = service.insertRole(role);
        System.out.println(s);
    }
}
