package priv.backend.domain.vo.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 响应前端服务器参数
 * @CreateDate :  2024-05-26 21:54
 */
@Data
public class RespServerParametersVO {
    /* TODO: Written by - Han Yongding 2024/05/26 系统名称 */
    private String systemName ;
    /* TODO: Written by - Han Yongding 2024/05/26 系统制造商 */
    private String systemManufacturer ;
    /* TODO: Written by - Han Yongding 2024/05/26 系统版本 */
    private String systemVersion ;
    /* TODO: Written by - Han Yongding 2024/05/26 系统类型 */
    private String systemType ;
    /* TODO: Written by - Han Yongding 2024/05/26 系统内部版本号 */
    private String systemInternalVersionNumber ;
    /* TODO: Written by - Han Yongding 2024/05/26 已用内存 */
    private Double usedMemory ;
    /* TODO: Written by - Han Yongding 2024/05/26 总内存 */
    private Double totalMemory ;
    /* TODO: Written by - Han Yongding 2024/05/26 处理器型号 */
    private String cpuModel ;
    /* TODO: Written by - Han Yongding 2024/05/26 处理器核心数 */
    private Integer cpuPhysicalProcessorNums ;
    /* TODO: Written by - Han Yongding 2024/05/26 处理器线程数 */
    private Integer cpuLogicProcessorNums ;
    /* TODO: Written by - Han Yongding 2024/05/26 处理器使用情况 */
    private Double cpuLoad ;
    /* TODO: Written by - Han Yongding 2024/05/26 系统盘总空间 */
    private Double systemTotalDiskSpace ;
    /* TODO: Written by - Han Yongding 2024/05/26 系统盘可用空间 */
    private Double systemAvailableDiskSpace ;
    /* TODO: Written by - Han Yongding 2024/05/26 项目所在磁盘总空间 */
    private Double projectTotalDiskSpace ;
    /* TODO: Written by - Han Yongding 2024/05/26 项目所在磁盘可用空间 */
    private Double projectAvailableDiskSpace ;
}
