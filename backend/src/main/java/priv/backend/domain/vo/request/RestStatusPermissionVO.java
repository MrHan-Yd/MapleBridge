package priv.backend.domain.vo.request;

import lombok.Data;
import priv.backend.domain.BaseData;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 接收前端提交的权限状态实体
 * @CreateDate :  2024-01-22 16:06
 */
@Data
public class RestStatusPermissionVO implements BaseData {
    private String statusName ;
}
