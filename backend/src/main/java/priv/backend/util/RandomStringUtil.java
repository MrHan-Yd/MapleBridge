package priv.backend.util;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 随机字符串工具类
 * @CreateDate :  2024-01-10 13:18
 */
public class RandomStringUtil {

    /** TODO: Written by - Han Yongding 2024/01/10 获取随机的六位数字字符串 */
    public static String getRandomSixDigitCode() {
        return RandomStringUtil.generateRandomNumericString(6) ;
    }

    /** TODO: Written by - Han Yongding 2024/01/10 根据长度生成只包含数字的随机字符串 */
    public static String generateRandomNumericString(int length) {
        return RandomStringUtils.random(length, "0123456789");
    }
}
