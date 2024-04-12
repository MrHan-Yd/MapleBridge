package priv.backend.domain.es.dto;

import lombok.Data;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : ES使用的个人信息DTO
 * @CreateDate :  2024-04-12 0:23
 */
@Data
public class ESProfile {
    private String profileId ;
    private String userId ;
    private String lastName ;
    private String firstName ;
    private String region ;
    private String location ;
    private String graduationDepartment ;
    private String major ;
    private String graduationYear ;
    private Date updateTime ;
}
