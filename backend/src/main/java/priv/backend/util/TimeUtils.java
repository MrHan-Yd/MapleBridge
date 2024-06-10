package priv.backend.util;

import lombok.Getter;

import java.time.LocalDateTime;


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

    public static String end() {
        return TimeUtils.getCurrentTime(TimeUtils.getStartTime()) ;
    }

    /**
     * TODO: Written by - Han Yongding 2024/05/28 在传入的时间上加上5分钟
     *
     * @param time 要添加五分钟的时间，不能为 null
     * @return 添加五分钟后的时间，返回LocalDateTime对象
     */
    public static LocalDateTime addFiveMinutes(LocalDateTime time) {
        // 检查传入的时间参数是否为 null
        if (time == null) {
            throw new IllegalArgumentException("传入的时间参数不能为 null");
        }
        // 在传入的时间上加上5分钟，并返回新的 LocalDateTime 对象
        return time.plusMinutes(5);
    }

    /**
     * TODO: Written by - Han Yongding 2024/05/28 在传入的时间上加上10分钟
     *
     * @param time 要添加十分钟的时间，不能为 null
     * @return 添加五分钟后的时间，返回LocalDateTime对象
     */
    public static LocalDateTime addThirtyMinutes(LocalDateTime time) {
        // 检查传入的时间参数是否为 null
        if (time == null) {
            throw new IllegalArgumentException("传入的时间参数不能为 null");
        }
        // 在传入的时间上加上5分钟，并返回新的 LocalDateTime 对象
        return time.plusMinutes(30);
    }

    /**
     * TODO: Written by - Han Yongding 2024/05/28 检查传入的时间是否已经过期
     *
     * @param time 要检查的时间，不能为 null
     * @return 如果当前时间在传入的时间之后，则返回 true；否则返回 false
     */
    public static boolean isExpired(LocalDateTime time) {
        // 检查传入的时间参数是否为 null
        if (time == null) {
            throw new IllegalArgumentException("传入的时间参数不能为 null");
        }
        // 检查当前时间是否在传入的时间之后，并返回结果
        return LocalDateTime.now().isAfter(time);
    }

}
