package priv.backend.util;

import lombok.extern.slf4j.Slf4j;
import priv.backend.enumeration.DataBaseEnum;
import priv.backend.enumeration.DoubleEnum;
import priv.backend.enumeration.IntegerEnum;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 常用工具类
 * @CreateDate :  2024-01-09 22:20
 */
@Slf4j
public class CurrentUtils {

    /** TODO: 2022/7/7 计算hashMap大小 */
    public static int computeMapSize(int size){
        return (int)((float)size / 0.75F + 1.0F);
    }

    /** TODO: 2022/7/7 隐藏邮箱 */
    public static String hideEmail(String email){
        return email.replaceAll("(?<=.{2}).(?=.*.@)", "*").replaceAll("(?<=(@.[*])).(?=.*\\.)", "*") ;
    }

    /** TODO: 2022/7/7 隐藏电话 */
    public static String hidePhone(String phone){
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2") ;
    }

    /** TODO: 2022/7/7 生成六位随机数 */
    public static String sixRandom(){
        return String.valueOf((int)((Math.random() * 9 + 1) * 100000)) ;
    }

    /** TODO: 2022/7/7 随机生成UUID */
    public static String getUuId(){
        return UUID.randomUUID().toString().replaceAll("-", "") ;
    }

    /** TODO: 2022/7/7 获取未绑定邮箱的字符串，用于存入数据库，保证数据库中eamil字段不为空 */
    public static String getUnboundE_EMail(String userName){
        return DataBaseEnum.UnboundE_EMail.getContents().replace("^", userName) ;
    }

    /** TODO: Written by - Han Yongding 2024/01/22 获取系统当前时间 */
    public static Timestamp getTheCurrentSystemTime() {
        return new Timestamp(System.currentTimeMillis()) ;
    }

    /** TODO: Written by - Han Yongding 2024/01/22 判断数据库插入和更新操作是否成功 */
    public static boolean isEmptyByDtoInsertOrUpdate(int result) {
        return result < 1 ;
    }

    /* TODO: Written by - Han Yongding 2024/07/02 字符串加反单引号 */

    public static String wrapWithBackticks(String originalString) {
        return String.format("`%s`", originalString);
    }

    /** TODO: Written by - Han Yongding 2024/02/11 判断字符串是否为空 */
    public static boolean isEmpty(String str) {
        return str == null ;
    }

    /* TODO: Written by - Han Yongding 2024/04/01 长度比较 */
    public static boolean sizeWhetherUnequal(long sizeA, long sizeB) {
        return sizeA != sizeB ;
    }

    /* TODO: Written by - Han Yongding 2024/04/15 版本号更新 */
    public static Double versionUpdate(Double version) {
        return version + DoubleEnum.VERSION.value;
    }

    /* TODO: Written by - Han Yongding 2024/04/15 计数更新 */
    public static Integer countUpdate(Integer count) {
        return count + IntegerEnum.COUNT.value;
    }

    /* TODO: Written by - Han Yongding 2024/07/01 拼接当前年月 */
    public static String getCurrentYearMonth(String tableName) {
        return tableName + "_" + TimeUtils.getCurrentYearMonth();
    }
}
