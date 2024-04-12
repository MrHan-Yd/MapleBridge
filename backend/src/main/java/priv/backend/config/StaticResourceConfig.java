package priv.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 静态资源配置
 * @CreateDate :  2024-04-05 22:29
 */
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Value("${spring.upload.file_path}")
    private String filePath ;

    @Value("${spring.upload.file_visit_path}")
    private String fileVisitPath ;

    /* TODO: Written by - Han Yongding 2024/04/05 静态资源 */
  @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
      registry.addResourceHandler("/upload/**")
              .addResourceLocations("file:" + filePath + fileVisitPath );
    }

}
