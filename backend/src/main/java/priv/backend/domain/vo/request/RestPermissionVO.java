package priv.backend.domain.vo.request;

import lombok.Data;
import priv.backend.domain.BaseData;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 接收前端新增权限表实体类
 * @CreateDate :  2024-01-27 15:31
 */
@Data
public class RestPermissionVO implements BaseData {
    private String permissionId ;
    private String permissionName ;
    private String permissionUrl ;
    private String statusId ;
    private String createId ;
}
