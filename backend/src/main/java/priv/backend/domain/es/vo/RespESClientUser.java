package priv.backend.domain.es.vo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import priv.backend.domain.dto.Profile;
import priv.backend.domain.dto.UserAvatars;
import priv.backend.domain.dto.UserLevel;
import priv.backend.domain.dto.UserProfile;
import priv.backend.domain.es.dto.ESProfile;
import priv.backend.domain.es.dto.ESUserAvatars;
import priv.backend.domain.es.dto.ESUserLevel;
import priv.backend.domain.es.dto.ESUserProfile;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : ES 响应前端获取用户数据
 * @CreateDate :  2024-04-07 13:03
 */
@Data
public class RespESClientUser {
    private String id ;
    private String nickname ;
    private String gender ;
    private Date birthday ;
    private Timestamp updateTime ;
    private Integer experience ;
    private ESUserAvatars avatars ;
    private ESUserProfile userProfile ;
    private ESUserLevel level ;
    private ESProfile profile ;
}
