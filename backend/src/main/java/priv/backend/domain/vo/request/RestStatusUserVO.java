package priv.backend.domain.vo.request;

import lombok.Data;
import priv.backend.domain.BaseData;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 接收前端用户状态实体类
 * @CreateDate :  2024-02-08 11:35
 */
@Data
public class RestStatusUserVO implements BaseData {
    private String statusName ;
}
