package priv.backend.domain.vo.response;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 响应前端公告状态
 * @CreateDate :  2024-03-04 13:07
 */
@Data
public class RespStatusAnnouncementVO {
    private String statusId ;
    private String statusName ;
    private String state ;
    private String createId ;
    private Timestamp createTime ;
    private String updateId ;
    private Timestamp updateTime ;
}
