package priv.backend.domain.es.dto;

import lombok.Data;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 帖子文件实体DTO
 * @CreateDate :  2024-04-03 22:19
 */
@Data
public class ESFilePost {
    private String fileId ;
    private String postId ;
    private String fileType ;
    private String fileName ;
    private String fileSuffix ;
    private Date fileTimestamp ;

}
