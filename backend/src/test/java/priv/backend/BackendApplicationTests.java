package priv.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import priv.backend.domain.PageBean;
import priv.backend.enumeration.StatusEnum;
import priv.backend.util.CurrentUtils;

import java.security.SecureRandom;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(StatusEnum.NORMAL.STATE);
    }

    /* TODO: Written by - Han Yongding 2023/09/06 生成一个Security安全key */
    @Test
    void KeyGenerator() {
        // 示例，生成一个长度为32的安全密钥
        String key = generateSecurityKey(32);
        System.out.println(key);
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateSecurityKey(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    /* TODO: Written by - Han Yongding 2024/03/09 分页实体测试 */
    @Test
    void PageTest() {
        PageBean pageBean = new PageBean() ;
        System.out.println(pageBean);
    }

    /* TODO: Written by - Han Yongding 2024/04/15 版本更新 */
    @Test
    void selfIncreasing() {
        Double a = 0.9 ;
        System.out.println(CurrentUtils.versionUpdate(a));
    }

    /* TODO: Written by - Han Yongding 2024/04/15 点赞更新 */
    @Test
    void updateLikes() {
        Integer a = 1 ;
        System.out.println(CurrentUtils.countUpdate(a));
    }


}
