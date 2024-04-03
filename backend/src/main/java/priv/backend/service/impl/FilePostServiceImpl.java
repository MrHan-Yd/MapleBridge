package priv.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import priv.backend.domain.dto.FilePost;
import priv.backend.mapper.FilePostMapper;
import priv.backend.service.FilePostService;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 帖子文件表业务层实现类
 * @CreateDate :  2024-04-03 12:28
 */
@Service
public class FilePostServiceImpl implements FilePostService {
    /* TODO: Written by - Han Yongding 2024/04/03 注入帖子文件表DAO */
    private final FilePostMapper filePostMapper ;

    @Autowired
    public FilePostServiceImpl(FilePostMapper filePostMapper) {
        this.filePostMapper = filePostMapper ;
    }


    /* TODO: Written by - Han Yongding 2024/04/03 新增帖子文件 */
    @Override
    public Integer batchInsertFilePost(List<FilePost> list) {
        return filePostMapper.insertBatchSomeColumn(list) ;
    }
}
