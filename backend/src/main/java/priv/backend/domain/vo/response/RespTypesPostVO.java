package priv.backend.domain.vo.response;

import lombok.Data;
import priv.backend.domain.BaseData;

import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 响应帖子类型VO
 * @CreateDate :  2024-03-08 12:53
 */
@Data
public class RespTypesPostVO implements BaseData {
    private String typeId ;
    private String typeName ;
    private String description ;
    private String createId ;
    private Timestamp createTime ;
    private String updateId ;
    private Timestamp updateTime ;
}
