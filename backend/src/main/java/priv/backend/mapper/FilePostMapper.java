package priv.backend.mapper;

import priv.backend.config.mybatis.MyBaseMapper;
import priv.backend.domain.dto.FilePost;
import priv.backend.domain.es.dto.ESFilePost;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 帖子文件表DAO
 * @CreateDate :  2024-04-03 12:25
 */
public interface FilePostMapper extends MyBaseMapper<FilePost> {

    /* TODO: Written by - Han Yongding 2024/04/03 根据帖子ID查询对应的文件 */
    List<ESFilePost> getFilePostByPostId(String postId) ;
}
