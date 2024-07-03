package priv.backend.enumeration;

import lombok.Getter;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 表名枚举类
 * @CreateDate :  2024-07-03 18:16
 */
@Getter
public enum TableNameEnum {
    LOGIN_LOG ("login_log"),
    REQUEST_LOG("request_log"),
    WEBSITE_TRAFFIC_LOG("website_traffic_log");

    private final String tableName;

    TableNameEnum(String tableName) {
        this.tableName = tableName;
    }
}
