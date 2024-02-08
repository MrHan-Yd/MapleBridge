package priv.backend.domain.vo.response;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 响应前端的用户状态实体类
 * @CreateDate :  2024-01-23 12:41
 */
@Data
public class RespStatusUserVO {
    private String statusId ;
    private String statusName ;
    private String state ;
    private String createId ;
    private Timestamp createTime ;
    private String updateId ;
    private Timestamp updateTime ;
}
