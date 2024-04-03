package priv.backend.service;

import priv.backend.domain.dto.FilePost;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 帖子文件表业务层
 * @CreateDate :  2024-04-03 12:27
 */
public interface FilePostService {

    /* TODO: Written by - Han Yongding 2024/04/03 批量新增帖子文件 */
    Integer batchInsertFilePost(List<FilePost> list) ;
}
