package priv.backend.mariadb.select;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import priv.backend.domain.vo.response.RespStatusRoleVO;
import priv.backend.service.impl.StatusRoleServiceImpl;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 角色状态查询
 * @CreateDate :  2024-01-23 13:44
 */
@SpringBootTest
public class StatusRoleTest {

    @Resource
    private StatusRoleServiceImpl service ;

    @Test
    public void queryStatusRole() {
        Page<RespStatusRoleVO> statusRoleVOPage = service.getAllStatusRoles(3, 10) ;
        statusRoleVOPage.getRecords().forEach(System.out::println);
        System.out.println("总记录数" + statusRoleVOPage.getTotal()) ;
        System.out.println("大小" + statusRoleVOPage.getSize()) ;
        System.out.println("页" + statusRoleVOPage.getCurrent()) ;
        System.out.println("总页数" + statusRoleVOPage.getPages()) ;
    }
}
