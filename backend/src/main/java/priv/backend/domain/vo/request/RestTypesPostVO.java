package priv.backend.domain.vo.request;

import lombok.Data;
import priv.backend.domain.BaseData;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 接受前端请求的帖子类型信息
 * @CreateDate :  2024-03-08 12:48
 */
@Data
public class RestTypesPostVO implements BaseData {
    private String typeId ;
    private String typeName ;
    private String description ;
    private String createId ;
    private String updateId ;
}
