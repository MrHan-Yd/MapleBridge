package priv.backend.domain.es.dto;

import lombok.Data;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : ES使用的用户头像DTO
 * @CreateDate :  2024-04-12 0:19
 */
@Data
public class ESUserAvatars {
    private String avatarsId ;
    private String userId ;
    private String avatarPath ;
    private Date updateTime ;
    private String fileName ;
    private String fileSize ;
}
