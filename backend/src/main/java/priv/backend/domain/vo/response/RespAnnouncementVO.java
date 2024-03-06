package priv.backend.domain.vo.response;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 响应前端公告VO
 * @CreateDate :  2024-03-04 20:39
 */
@Data
public class RespAnnouncementVO {
    private String id ;
    private String title ;
    private String content ;
    private Timestamp[] releaseAndDeadline ;
    private String publisherId ;
    private RespTypesAnnouncementSelectVO type ;
    private String statusId ;
    private String createId ;
    private Timestamp createTime ;
    private String updateId ;
    private Timestamp updateTime ;

}
