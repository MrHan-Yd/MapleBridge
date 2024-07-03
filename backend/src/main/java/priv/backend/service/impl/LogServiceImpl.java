package priv.backend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import priv.backend.domain.PageBean;
import priv.backend.domain.dto.Log;
import priv.backend.enumeration.RespMessageEnum;
import priv.backend.mapper.LogMapper;
import priv.backend.service.LogService;
import priv.backend.util.CurrentUtils;
import priv.backend.util.LogUtils;
import priv.backend.util.TimeUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 日志业务层实现类
 * @CreateDate :  2024-06-27 15:47
 */
@Service
public class LogServiceImpl implements LogService {
    @Resource
    private LogMapper mapper;

    /* TODO: Written by - Han Yongding 2024/06/27 获取平台日志表 */
    @Override
    public Page<Log> getLogs(PageBean pageBean) {
        /* TODO: Written by - Han Yongding 2024/07/02 分页 */
        Page<Log> page = new Page<>(pageBean.getPageNum(), pageBean.getPageSize());
        return mapper.getLogs(page);
    }

    /* TODO: Written by - Han Yongding 2024/06/29 备份日志表 */
    @Override
    public String backupLogs(String tableName) {
        /* TODO: Written by - Han Yongding 2024/06/29 判空 */
        if (ObjectUtils.isEmpty(tableName)) {
            return "参数不能为空";
        }
        /* TODO: Written by - Han Yongding 2024/06/29 生成备份表名 */
        String backupTableName = CurrentUtils.getCurrentYearMonth(tableName);
        String comment = backupTableName + "备份";

        /* TODO: Written by - Han Yongding 2024/06/29 本月已备份 */
        if (this.checkTableExists(backupTableName)) {
            return "本月已备份";
        }

        backupTableName = CurrentUtils.wrapWithBackticks(backupTableName);
        tableName = CurrentUtils.wrapWithBackticks(tableName);

        /* TODO: Written by - Han Yongding 2024/06/29 备份日志表 */
        Integer rs = mapper.backupLogs(tableName, backupTableName, comment);
        if (rs < 0) {
            return "备份失败";
        }

        /* TODO: Written by - Han Yongding 2024/06/29 查询备份表数据数量 */
        if (!Objects.equals(this.countLogs(tableName), rs)) {
            return "备份失败，备份数据条数不一致";
        }

        /* TODO: Written by - Han Yongding 2024/06/29 备份成功 */
        return null;
    }

    /* TODO: Written by - Han Yongding 2024/06/29 统计日志表数据条数 */
    private Integer countLogs(String tableName) {
        return mapper.countLogs(tableName);
    }

    /* TODO: Written by - Han Yongding 2024/06/29 查询数据表是否存在 */
    private boolean checkTableExists(String tableName) {
        return mapper.checkTableExists(tableName) != 0;
    }

    @Override
    public String truncateLogs(String tableName) {
        if (ObjectUtils.isEmpty(tableName)) {
            return RespMessageEnum.PARAM_NOT_NULL.getMessage();
        }

        /* TODO: Written by - Han Yongding 2024/07/01 已备份，可以清空，但是确保是日志表之一 */
        List<String> list = mapper.getTableName();
        if (!list.contains(tableName)) {
            return "异常表名，无法清空";
        }

        String backupTableName = CurrentUtils.getCurrentYearMonth(tableName);
        /* TODO: Written by - Han Yongding 2024/07/01 本月没有备份 */
        if (!this.checkTableExists(backupTableName)) {
            return "未备份，不能清空";
        }

        /* TODO: Written by - Han Yongding 2024/07/02 包含，可以清空 */
        mapper.truncateTableByName(tableName);
        return null;
    }

    @Override
    public Page<Log> getBackupLogs(PageBean pageBean) {
        Page<Log> page = new Page<>(pageBean.getPageNum(), pageBean.getPageSize());
        return mapper.getBackupLogs(page) ;
    }

    @Override
    public String dropBackupTableByName(String backupTableName) {
        if (ObjectUtils.isEmpty(backupTableName)) {
            return RespMessageEnum.PARAM_NOT_NULL.getMessage();
        }

        /* TODO: Written by - Han Yongding 2024/07/02 判断是否是备份日志表 */
        List<String> list = mapper.getBackupTableName();
        if (!list.contains(backupTableName)) {
            return "异常表名，无法删除";
        }
        backupTableName = CurrentUtils.wrapWithBackticks(backupTableName);
        /* TODO: Written by - Han Yongding 2024/07/02 删除备份日志表 */
        try {
            mapper.deleteBackupTableByName(backupTableName);
        } catch (Exception e) {
            return "删除失败";
        }

        /* TODO: Written by - Han Yongding 2024/07/02 删除成功 */
        return null;
    }

    @Override
    public List<Map<String, Object>> getBackupLogData(String backupTableName) {
        if (ObjectUtils.isEmpty(backupTableName)) {
            return null;
        }

        /* TODO: Written by - Han Yongding 2024/07/02 判断是否是备份日志表 */
        List<String> list = mapper.getBackupTableName();
        if (!list.contains(backupTableName)) {
            return null;
        }
        backupTableName = CurrentUtils.wrapWithBackticks(backupTableName);

        return mapper.getBackupLogData(backupTableName) ;
    }

    @Override
    public String batchDropBackupLogs(List<String> names) {
        if (CollectionUtils.isEmpty(names)) {
            return RespMessageEnum.PARAM_NOT_NULL.getMessage();
        }

        /* TODO: Written by - Han Yongding 2024/07/02 判断是否是备份日志表 */
        List<String> list = mapper.getBackupTableName();
        for (String string : names) {
            if (!list.contains(string)) {
                return "异常表名，无法删除";
            }
        }

        List<String> nameList = names.stream().map(CurrentUtils::wrapWithBackticks).toList();
        /* TODO: Written by - Han Yongding 2024/07/02 批量删除备份日志表 */
        try {
            nameList.forEach(mapper::deleteBackupTableByName);
        } catch (Exception e) {
            return "删除失败";
        }

        /* TODO: Written by - Han Yongding 2024/07/02 删除成功 */
        return null;
    }

    @Override
    public Map<String, List<Map<String, Object>>> batchExportLogs(List<String> tableNames) {
        if (ObjectUtils.isEmpty(tableNames)) {
            return null;
        }

        /* TODO: Written by - Han Yongding 2024/07/02 判断是否是备份日志表 */
        List<String> list = mapper.getBackupTableName();
        for (String tableName : tableNames) {
            if (!list.contains(tableName)) {
                return null;
            }
        }

        /* TODO: Written by - Han Yongding 2024/07/02 处理表名 */
        List<String> nameList = tableNames.stream().map(CurrentUtils::wrapWithBackticks).toList();
        /* TODO: Written by - Han Yongding 2024/07/03 使用并发工具CompletableFuture */
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        /* TODO: Written by - Han Yongding 2024/07/03 创建一个线程安全的Map来存储查询结果 */
        Map<String, List<Map<String, Object>>> map = new ConcurrentHashMap<>();

        /* TODO: Written by - Han Yongding 2024/07/03 并发查询每个表的日志数据 */
        for (String tableName : nameList) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    /* TODO: Written by - Han Yongding 2024/07/03 查询日志数据，并存储到线程安全的Map中 */
                    List<Map<String, Object>> data = mapper.getBackupLogData(tableName);
                    map.put(tableName.substring(1, tableName.length() - 1), data);
                } catch (Exception e) {
                    LogUtils.error(this.getClass(), e.getMessage());
                }
            });
            futures.add(future) ;
        }

        /* TODO: Written by - Han Yongding 2024/07/03 等待所有查询操作完成 */
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join() ;

        /* TODO: Written by - Han Yongding 2024/07/02 业务结束，返回数据 */
        return map;
    }

}
