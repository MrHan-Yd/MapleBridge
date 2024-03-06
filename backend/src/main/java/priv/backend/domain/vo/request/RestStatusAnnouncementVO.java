package priv.backend.domain.vo.request;

import lombok.Data;
import priv.backend.domain.BaseData;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 接收前端公告实体VO
 * @CreateDate :  2024-03-04 12:56
 */
@Data
public class RestStatusAnnouncementVO implements BaseData {
    private String statusId ;
    private String statusName ;
    private String createId ;
    private String state ;
    private String updateId;
}
