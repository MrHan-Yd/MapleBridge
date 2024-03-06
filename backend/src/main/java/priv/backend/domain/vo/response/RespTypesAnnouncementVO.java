package priv.backend.domain.vo.response;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 响应公告类型VO
 * @CreateDate :  2024-03-04 17:51
 */
@Data
public class RespTypesAnnouncementVO {
    private String typeId ;
    private String typeName ;
    private String description ;
    private String createId ;
    private Timestamp createTime ;
    private String updateId ;
    private Timestamp updateTime ;
}
