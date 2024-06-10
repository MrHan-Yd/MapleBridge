//package priv.backend.mariadb.select;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import jakarta.annotation.Resource;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import priv.backend.domain.dto.Role;
//import priv.backend.mapper.RoleMapper;
//
///**
// * Created by IntelliJ IDEA.
// *
// * @author : weiguang
// * @Description : 作用
// * @CreateDate :  2024-01-31 12:01
// */
//@SpringBootTest
//public class RoleTest {
//
//    @Resource
//    private RoleMapper mapper ;
//
//    /* TODO: Written by - Han Yongding 2024/01/31 分页查询所有角色 */
//    @Test
//    void queryRole() {
//        mapper.getRole(new Page<>(1, 10))
//                .getRecords()
//                .forEach(System.out::println); ;
//    }
//}
