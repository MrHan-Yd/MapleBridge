package priv.backend;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import priv.backend.util.Ip2RegionUtils;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 获取IP地址测试类
 * @CreateDate :  2024-06-08 22:43
 */
@SpringBootTest
public class IpTest {

    @Resource
    private Ip2RegionUtils regionUtils ;

    @Test
    public void testGetIpInfo() {
        String ip = "0:0:0:0:0:0:0:1";
        String ipPosition = regionUtils.getIpPosition(ip);
        System.out.println(ipPosition);
//        System.out.println(regionUtils.getIpInformation(ipPosition));
    }
}
