package priv.backend.domain.vo.response;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 响应前端分页查询用户实体类
 * @CreateDate :  2024-02-12 23:41
 */
@Data
public class RespUserVO {
    private String id ;
    private String account ;
    private String email ;
    private String nickname ;
    private String gender ;
    private Date birthday ;
    private Integer experience ;
    private Timestamp registerTime ;
    private String statusId ;
    private String createId ;
    private Timestamp createTime ;
    private String updateId ;
    private Timestamp updateTime ;
    private RespUserLevelVO level ;
    private RespUserRoleVO role ;
}
