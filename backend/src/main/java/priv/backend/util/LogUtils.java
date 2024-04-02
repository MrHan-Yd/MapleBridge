package priv.backend.util;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 日志常用工具类
 * @CreateDate :  2024-04-01 17:22
 */
@Slf4j
public class LogUtils {

    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void info(Class<?> clazz,String message) {
        log.info("{} - [ {} : {} ]", getCaller(clazz), "通知", message);
    }
    public static void infoCustom(Class<?> clazz, String type, String message) {
        log.info("{} - [ {} : {} ]", getCaller(clazz), type, message);
    }

    public static void warning(Class<?> clazz, String message) {
        log.warn("{} - [ {} : {} ]", getCaller(clazz), "警告", message);
    }

    public static void warningCustom(Class<?> clazz, String type, String message) {
        log.warn("{} - [ {} : {} ]", getCaller(clazz), type, message);
    }

    public static void debug(Class<?> clazz, String message) {
        log.debug("{} - [ {} : {} ]", getCaller(clazz), "测试", message);
    }

    public static void debugCustom(Class<?> clazz, String type, String message) {
        log.debug("{} - [ {} : {} ]", getCaller(clazz), type, message);
    }

    public static void error(Class<?> clazz, String message) {
        log.error("{} - [ {} : {} ]", getCaller(clazz), "错误", message);
    }

    public static void errorCustom(Class<?> clazz, String type, String message) {
        log.error("{} - [ {} : {} ]", getCaller(clazz), type, message);
    }

    private static String getCaller(Class<?> clazz) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().equals(clazz.getName())) {
                return ANSI_BLUE + element.getClassName() + "." + element.getMethodName() + " 第 " + element.getLineNumber() + " 行" + ANSI_RESET;
            }
        }
        return "Unknown";
    }
}
