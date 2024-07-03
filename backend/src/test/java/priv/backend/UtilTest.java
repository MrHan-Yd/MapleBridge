package priv.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import priv.backend.enumeration.RespMessageEnum;

import java.time.LocalDate;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 工具类测试
 * @CreateDate :  2024-06-29 15:39
 */
@SpringBootTest
public class UtilTest {

    @Test
    void test() {
        String yearMonth = LocalDate.now().toString().substring(0, 7);
        System.out.println(yearMonth);
    }

    @Test
    void test2() {
        RespMessageEnum.SUCCESS
        System.out.println();
    }
}
