package priv.backend.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 日志实体
 * @CreateDate :  2024-06-27 15:39
 */
@Data
public class Log {
    /* 表名 */
    private String tableName;
    /* 表类型 */
    private String tableType;
    /* 表引擎 */
    private String engine;
    /* 表版本 */
    private String version;
    /* 表中的数据行数 */
    private String tableRows;
    /* 平均每行数据长度 */
    private String avgRowLength;
    /* 表数据长度 */
    private String dataLength;
    /* 表索引长度 */
    private String indexLength;
    /* 表创建时间 */
    private LocalDateTime createTime;
    /* 表最后一次更新时间 */
    private LocalDateTime updateTime;
    /* 表的排序规则(字符集) */
    private String tableCollation;
    /* 表的注释 */
    private String tableComment;
}
