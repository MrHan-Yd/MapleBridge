package priv.backend.domain.es.dto;


import lombok.Data;


import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : ES使用的用户简介DTO
 * @CreateDate :  2024-04-12 0:15
 */
@Data
public class ESUserProfile {
    private String profileId ;
    private String bio ;
    private Date updateTime ;
}
