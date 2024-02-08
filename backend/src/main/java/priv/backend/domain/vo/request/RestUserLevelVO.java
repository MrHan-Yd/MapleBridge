package priv.backend.domain.vo.request;

import lombok.Data;
import priv.backend.domain.BaseData;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 接受前端请求的用户等级表实体类
 * @CreateDate :  2024-02-06 17:43
 */
@Data
public class RestUserLevelVO implements BaseData {
    private String levelId ;
    private String levelName;
    private Integer level;
    private Integer requiredExperience;
    private String privilegeDescription;
}
