package priv.backend.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import priv.backend.domain.Files;
import priv.backend.domain.Upload;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 上传工具类
 * @CreateDate :  2024-04-03 9:33
 */
@Component
public class UploadUtils {

    private final Upload upload ;

    @Autowired
    public UploadUtils(Upload upload) {
        this.upload = upload ;
    }

    /* TODO: Written by - Han Yongding 2024/04/03 生成访问路径 */
    public String generateAccessPath(String userId, String path) {
        return upload.getFileHost() +
                upload.getFileVisitPath() +
                userId + "/" + path + "/" ;
    }


    /* TODO: Written by - Han Yongding 2024/04/03 生成保存路径 */
    public String generateSavePath(String userId, String path) {
        return upload.getFilePath() +
                upload.getFileVisitPath() +
                userId + "/" + path + "/" ;
    }

    /** TODO: Written by - Han Yongding 2024/04/03 写出文件 */
    public Files saveFile(MultipartFile file, String path) {
        /* TODO: Written by - Han Yongding 2024/04/03 没有上传文件 */
        if (file.isEmpty()) {
            return null ;
        }

        /* TODO: Written by - Han Yongding 2024/04/03 创建目录，失败则返回null */
        File file1 = this.createFile(path) ;
        if(file1 == null) {
            return null ;
        }

        /* TODO: Written by - Han Yongding 2024/04/03 获取原文件名的后缀名 */
        String name = file.getOriginalFilename() ;

        /* TODO: Written by - Han Yongding 2024/04/03 后缀名  */
        String suffix;
        if (name == null) {
            /* TODO: Written by - Han Yongding 2024/04/03 文件名为空无法写入 */
            return null ;
        }
        /* TODO: Written by - Han Yongding 2024/04/03 后缀名 */
        suffix = name.substring(name.lastIndexOf("."));
        /* TODO: Written by - Han Yongding 2024/04/03 随机生成文件名 */
        String randomName = CurrentUtils.getUuId() ;
        /* TODO: Written by - Han Yongding 2024/04/03 拼接成完整的文件名 */
        String fileName = randomName + suffix ;

        File sava = new File(path  + fileName) ;
        try {
            /* TODO: Written by - Han Yongding 2024/04/03 写入成功 */
            file.transferTo(sava);
        } catch (Exception e) {
            /* TODO: Written by - Han Yongding 2024/04/03 写入失败 */
            return null ;
        }

        /* TODO: Written by - Han Yongding 2024/04/03 返回对象 */

        return new Files(randomName, suffix, file.getContentType(), CurrentUtils.getTheCurrentSystemTime());
    }

    /* TODO: Written by - Han Yongding 2024/04/03 批量写出文件 */
    public List<Files> batchSaveFile(List<MultipartFile> list, String path) throws IOException{
        /* TODO: Written by - Han Yongding 2024/04/03 没有上传文件 */
        if (list.isEmpty()) {
            return null ;
        }

        /* TODO: Written by - Han Yongding 2024/04/03 创建目录，失败则返回null */
        File file1 = this.createFile(path) ;
        if(file1 == null) {
            return null ;
        }

        /* TODO: Written by - Han Yongding 2024/04/03 需要返回的信息 */
        List<Files> filesList = new ArrayList<>() ;

        for (MultipartFile file : list) {
            /* TODO: Written by - Han Yongding 2024/04/03 随机生成文件名 */
            String randomName = CurrentUtils.getUuId() ;

            String name = file.getOriginalFilename() ;

            assert name != null ;

            /* TODO: Written by - Han Yongding 2024/04/03 后缀名 */
            String suffix = name.substring(name.lastIndexOf("."));

            /* TODO: Written by - Han Yongding 2024/04/03 拼接成完整的文件名 */
            String fileName = randomName + suffix ;

            File sava = new File(path + fileName) ;
            /* TODO: Written by - Han Yongding 2024/04/03 写入成功 */
            file.transferTo(sava);
            filesList.add(new Files(randomName, suffix, file.getContentType(), CurrentUtils.getTheCurrentSystemTime())) ;
        }

        return filesList ;
    }

    /* TODO: Written by - Han Yongding 2024/04/03 判断是否需要创建目录 */
    private File createFile(String path){
        File file = new File(path) ;
        /* TODO: Written by - Han Yongding 2024/04/03 目录不存在则创建 */
        if (!file.exists()) {
            /* TODO: Written by - Han Yongding 2024/04/03 创建目录，创建失败则返回null */
            if (!file.mkdirs()) {
                return null ;
            }
        }
        return file ;
    }

}
