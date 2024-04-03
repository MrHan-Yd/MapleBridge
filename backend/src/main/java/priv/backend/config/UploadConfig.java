package priv.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import priv.backend.domain.Upload;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 上传Bean
 * @CreateDate :  2024-04-03 10:16
 */
@Configuration
public class UploadConfig {

    @Value("${spring.upload.file_path}")
    private String filePath ;

    @Value("${spring.upload.file_visit_path}")
    private String fileVisitPath ;

    @Value("${spring.upload.file_host}")
    private String fileHost ;

    /* TODO: Written by - Han Yongding 2024/04/03 上传配置信息 */
    @Bean
    public Upload uploadBean() {
        return new Upload(filePath, fileVisitPath, fileHost) ;
    }

}
