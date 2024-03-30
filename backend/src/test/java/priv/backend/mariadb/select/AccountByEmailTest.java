package priv.backend.mariadb.select;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import priv.backend.service.impl.AccountServiceImpl;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 作用
 * @CreateDate :  2024-03-28 16:37
 */
@SpringBootTest
public class AccountByEmailTest {
    @Resource
    AccountServiceImpl accountService ;
    @Test
    void byEmail() {
        System.out.println(accountService.existsAccountByUserName("admin"));
    }
}
