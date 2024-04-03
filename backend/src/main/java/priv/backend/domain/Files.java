package priv.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 文件实体类
 * @CreateDate :  2024-04-03 13:30
 */
@Data
@AllArgsConstructor
public class Files implements BaseData{
    private String fileName ;
    private String fileSuffix ;
    private String fileType ;
    private Timestamp fileTimestamp ;
}
