package priv.backend.domain.vo.request;

import lombok.Data;
import priv.backend.domain.BaseData;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 接收前端修改用户状态的状态
 * @CreateDate :  2024-01-24 17:13
 */
@Data
public class RestUserStateVO implements BaseData {
    private String statusId ;
    private String state ;
    private String statusName ;
    private String createId ;
    private String updateId ;
}
