package priv.backend.util;

import lombok.Getter;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 时间工具类
 * @CreateDate :  2024-04-01 16:47
 */
public class TimeUtils {
    @Getter
    private static long startTime;

    public static void start() {
        startTime = System.currentTimeMillis();
    }

    /** TODO: 2022/8/30 耗时计算(秒或毫秒) */
    public static String getCurrentTime(long time){
        return (System.currentTimeMillis() - time)  / 1000 > 0 ? (System.currentTimeMillis() - time) / 1000 + "秒" : System.currentTimeMillis() - time + "毫秒" ;
    }

}
