package priv.backend.domain.vo.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import priv.backend.domain.dto.TypesAnnouncement;

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
