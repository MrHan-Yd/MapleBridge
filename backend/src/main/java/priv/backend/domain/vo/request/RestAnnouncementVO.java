package priv.backend.domain.vo.request;

import lombok.Data;
import priv.backend.domain.BaseData;

import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 接收前端公告VO
 * @CreateDate :  2024-03-04 20:39
 */
@Data
public class RestAnnouncementVO implements BaseData {
    private String id ;
    private String title ;
    private String content ;
    private Timestamp[] releaseAndDeadline ;
    private String publisherId ;
    private String typeId ;
    private String statusId ;
    private String createId ;
    private String updateId ;

}
