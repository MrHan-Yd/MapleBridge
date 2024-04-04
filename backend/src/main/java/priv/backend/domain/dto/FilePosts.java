package priv.backend.domain.dto;

import lombok.Data;
import priv.backend.domain.BaseData;


/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 帖子文件表同步ES使用
 * @CreateDate :  2024-04-03 12:23
 */
@Data
public class FilePosts implements BaseData {
    private String fileId ;
    private String postId ;
    private String fileType ;
    private String fileName ;
    private String fileSuffix ;
    private String userId ;
}
