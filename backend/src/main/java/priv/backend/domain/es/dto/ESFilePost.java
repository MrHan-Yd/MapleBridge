package priv.backend.domain.es.dto;

import lombok.Data;


/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 帖子文件实体DTO(ES使用)
 * @CreateDate :  2024-04-03 22:19
 */
@Data
public class ESFilePost {
    private String fileId ;
    private String postId ;
    private String fileType ;
    private String url ;
}
