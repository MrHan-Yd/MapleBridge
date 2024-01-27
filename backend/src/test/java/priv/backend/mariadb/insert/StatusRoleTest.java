package priv.backend.mariadb.insert;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import priv.backend.domain.dto.StatusRole;
import priv.backend.domain.vo.request.RestStatusRoleVO;
import priv.backend.service.StatusRoleService;
import priv.backend.util.CurrentUtils;

import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 角色状态插入测试
 * @CreateDate :  2024-01-22 16:21
 */
@SpringBootTest
public class StatusRoleTest {

    @Autowired
    private StatusRoleService service ;

    /* TODO: Written by - Han Yongding 2024/01/22 插入角色状态 */
    @Test
    public void insertStatusRole() {
        RestStatusRoleVO vo = new RestStatusRoleVO() ;
        vo.setStatusName("在用") ;
//        vo.setState('0') ;
//        vo.setCreateTime(CurrentUtils.getTheCurrentSystemTime()) ;
        System.out.println(vo) ;
        System.out.println(service.insertStatusRole(vo));
    }
}
