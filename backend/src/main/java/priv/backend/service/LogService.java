package priv.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import priv.backend.domain.PageBean;
import priv.backend.domain.dto.Log;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 日志业务层
 * @CreateDate :  2024-06-27 15:47
 */
public interface LogService {

    /* TODO: Written by - Han Yongding 2024/06/27 获取平台日志表 */
    Page<Log> getLogs(PageBean pageBean) ;

    /* TODO: Written by - Han Yongding 2024/06/29 备份日志表 */
    String backupLogs(String tableName) ;

    /* TODO: Written by - Han Yongding 2024/07/01 清空表 */
    String truncateLogs(String tableName) ;

    /* TODO: Written by - Han Yongding 2024/07/02 获取平台备份日志表 */
    Page<Log> getBackupLogs(PageBean pageBean) ;

    /* TODO: Written by - Han Yongding 2024/07/02 删除备份日志表 */
    String dropBackupTableByName(String backupTableName) ;

    /* TODO: Written by - Han Yongding 2024/07/02 批量删除备份日志表 */
    String batchDropBackupLogs(List<String> names) ;

    /* TODO: Written by - Han Yongding 2024/07/02 查询备份日志表数据 */
    List<Map<String, Object>> getBackupLogData(String backupTableName) ;

    /* TODO: Written by - Han Yongding 2024/07/02 批量导出日志表 */
    Map<String, List<Map<String, Object>>> batchExportLogs(List<String> tableNames) ;


}
