package priv.backend.domain.vo.request;

import lombok.Data;
import priv.backend.domain.BaseData;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 接受前端用户请求实体类
 * @CreateDate :  2024-02-11 12:48
 */
@Data
public class RestUserVO implements BaseData {
    private String id ;
    private String account ;
    private String email ;
    private String password ;
    private String roleId ;
    private String statusId ;
    private String createId ;
}
