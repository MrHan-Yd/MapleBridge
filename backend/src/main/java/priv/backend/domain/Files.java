package priv.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 文件实体类，存储写出的文件数据，用于持久化到数据库中
 * @CreateDate :  2024-04-03 13:30
 */
@Data
@AllArgsConstructor
public class Files implements BaseData {
    private String fileName ;
    private String fileSuffix ;
    private String fileType ;
    private Date fileTimestamp ;
}
