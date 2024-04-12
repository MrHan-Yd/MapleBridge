package priv.backend.domain.vo.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 接收客户端用户信息VO
 * @CreateDate :  2024-04-10 22:32
 */
@Data
public class RestClientUserVO {
    private String id ;
    private MultipartFile avatars ;
    private String nickname ;
    private String gender ;
    private Date birthday ;
    private String profileId ;
    private String bio ;
    private String lastName ;
    private String firstName ;
    private String region ;
    private String location ;
    private String graduationDepartment ;
    private String major ;
    private String graduationYear ;
}
