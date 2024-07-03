package priv.backend.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import priv.backend.domain.dto.Log;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 日志Mapper
 * @CreateDate :  2024-06-27 15:46
 */
public interface LogMapper {

    /* TODO: Written by - Han Yongding 2024/06/27 分页获取平台日志表 */
    Page<Log> getLogs(Page<Log> page) ;

    /* TODO: Written by - Han Yongding 2024/07/01 获取所有日志表名 */
    List<String> getTableName() ;

    /* TODO: Written by - Han Yongding 2024/07/02 分页获取备份日志表 */
    Page<Log> getBackupLogs(Page<Log> page) ;

    /* TODO: Written by - Han Yongding 2024/07/02 获取所有备份日志表名 */
    List<String> getBackupTableName() ;

    /* TODO: Written by - Han Yongding 2024/07/02 删除备份日志表 */
    void deleteBackupTableByName(String backupTableName) ;

    /* TODO: Written by - Han Yongding 2024/06/29 备份日志表 */
    Integer backupLogs(@Param("tableName") String tableName, @Param("backupTableName") String backupTableName, @Param("comment") String comment) ;

    /* TODO: Written by - Han Yongding 2024/06/29 统计日志表数据条数 */
    Integer countLogs(String tableName) ;

    /* TODO: Written by - Han Yongding 2024/06/29 查询数据表是否存在 */
    Integer checkTableExists(String tableName) ;

    /* TODO: Written by - Han Yongding 2024/07/01 清空表 */
    void truncateTableByName(@Param("tableName") String tableName) ;

    /* TODO: Written by - Han Yongding 2024/07/02 查询备份日志表数据 */
    List<Map<String, Object>> getBackupLogData(String backupTableName) ;

}
