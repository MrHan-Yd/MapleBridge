package priv.backend.domain.vo.request;

import lombok.Data;
import priv.backend.domain.BaseData;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 接收前端新增工作类型或修改工作类型
 * @CreateDate :  2024-02-28 20:21
 */
@Data
public class RestTypesWorkExperienceVO implements BaseData {
    private String typeId ;
    private String typeName ;
    private String description ;
}
