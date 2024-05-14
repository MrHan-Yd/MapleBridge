package priv.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan({"priv.backend.mapper"})
@EnableScheduling //开启定时任务支持
//@EnableAspectJAutoProxy //开启AOP支持，SpringBoot自动装配，不需要添加
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
