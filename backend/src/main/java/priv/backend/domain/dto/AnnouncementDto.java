package priv.backend.domain.dto;

import lombok.Data;
import priv.backend.domain.BaseData;
import priv.backend.domain.vo.response.RespTypesAnnouncementSelectVO;

import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 公告自定义查询DTO
 * @CreateDate :  2024-03-07 0:41
 */
@Data
public class AnnouncementDto implements BaseData {
    private String id ;
    private String title ;
    private String content ;
    private Timestamp publishTime ;
    private String publisherId ;
    private Timestamp deadline ;
    private RespTypesAnnouncementSelectVO type ;
    private String statusId ;
    private String createId ;
    private Timestamp createTime ;
    private String updateId ;
    private Timestamp updateTime ;
}
