package priv.backend.domain.vo.request;

import lombok.Data;
import priv.backend.domain.BaseData;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 接受请求公告管理VO
 * @CreateDate :  2024-03-04 18:00
 */
@Data
public class RestTypesAnnouncementVO implements BaseData {
    private String typeId ;
    private String typeName ;
    private String description ;
    private String createId ;
    private String updateId ;
}
