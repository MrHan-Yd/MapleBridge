package priv.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 配置信息
 * @CreateDate :  2024-04-03 10:23
 */
@Data
@AllArgsConstructor
public class Upload {
    private String filePath ;
    private String fileVisitPath ;
    private String fileHost ;
}
