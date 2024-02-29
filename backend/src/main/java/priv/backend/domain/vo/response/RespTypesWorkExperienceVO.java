package priv.backend.domain.vo.response;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 响应前端工作类型VO
 * @CreateDate :  2024-02-28 12:40
 */
@Data
public class RespTypesWorkExperienceVO {
    private String typeId ;
    private String typeName ;
    private String description ;
    private String createId ;
    private Timestamp createTime ;
    private String updateId ;
    private Timestamp updateTime ;
}
