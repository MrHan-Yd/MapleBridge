package priv.backend.domain.dto;

import lombok.Data;

import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 客户端使用USER
 * @CreateDate :  2024-04-06 12:38
 */
@Data
public class ClientUser {
    private String id ;
    private String nickname ;
    private String gender ;
    private Date birthday ;
    private Date updateTime ;
    private Integer experience ;
    private UserAvatars avatars ;
    private UserProfile userProfile ;
    private UserLevel level ;
    private Profile profile ;
}
