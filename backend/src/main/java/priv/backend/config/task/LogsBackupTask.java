package priv.backend.config.task;

import jakarta.annotation.Resource;
import priv.backend.annotations.Task;
import priv.backend.annotations.TaskClass;
import priv.backend.enumeration.TableNameEnum;
import priv.backend.service.impl.LogServiceImpl;
import priv.backend.util.CurrentUtils;
import priv.backend.util.LogUtils;
import priv.backend.util.TimeUtils;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 日志备份任务
 * @CreateDate :  2024-07-03 18:09
 */
@TaskClass
public class LogsBackupTask {

    @Resource
    private LogServiceImpl service ;

    /* TODO: Written by - Han Yongding 2024/07/03 登录日志备份任务 */
    @Task(taskName = "login-logs-backup-task",taskRemarks = "登录日志备份任务")
    public void loginLogsBackupTask() {
        this.LogBackupTask(TableNameEnum.LOGIN_LOG.getTableName(), "登录日志");
    }

    /* TODO: Written by - Han Yongding 2024/07/03 请求日志备份任务 */
    @Task(taskName = "request-logs-backup-task",taskRemarks = "请求日志备份任务")
    public void requestLogsBackupTask() {
        this.LogBackupTask(TableNameEnum.REQUEST_LOG.getTableName(), "请求日志");
    }

    /* TODO: Written by - Han Yongding 2024/07/03 网站流量日志备份任务 */
    @Task(taskName = "website-traffic-logs-backup-task",taskRemarks = "网站流量日志备份任务")
    public void WebsiteTrafficLogsBackupTask() {
        this.LogBackupTask(TableNameEnum.WEBSITE_TRAFFIC_LOG.getTableName(), "网站流量日志");
    }

    private void LogBackupTask(String tableName, String TableNameCn) {
        TimeUtils.start();
        LogUtils.info(this.getClass(), TableNameCn + "备份任务开始执行");

        /* TODO: Written by - Han Yongding 2024/07/03 备份 */
        if(service.backupLogs(tableName) != null) {
            LogUtils.error(this.getClass(), TableNameCn + "备份失败");
        } else {
            LogUtils.info(this.getClass(), TableNameCn + "备份成功");
            /* TODO: Written by - Han Yongding 2024/07/03 清空日志表 */
            if(service.truncateLogs(tableName) != null) {
                LogUtils.info(this.getClass(), TableNameCn + "清空失败，日志丢失，无法回滚");
            } else {
                LogUtils.info(this.getClass(), TableNameCn + "清空成功");
            }
        }
        LogUtils.info(this.getClass(), "本次任务执行耗时:" + TimeUtils.end());
        LogUtils.info(this.getClass(), TableNameCn + "备份任务执行完毕");
    }
}
