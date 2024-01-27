package priv.backend.domain.vo.request;

import lombok.Data;
import priv.backend.domain.BaseData;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 接收前端新增权限表实体类
 * @CreateDate :  2024-01-27 15:31
 */
@Data
public class RestPermissionVO implements BaseData {
    private String permissionName ;
    private String permissionUrl ;
}
