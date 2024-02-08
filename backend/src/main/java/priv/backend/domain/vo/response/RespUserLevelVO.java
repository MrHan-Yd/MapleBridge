package priv.backend.domain.vo.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import priv.backend.domain.BaseData;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 响应前端的用户等级表实体类
 * @CreateDate :  2024-02-06 17:43
 */
@Data
public class RespUserLevelVO implements BaseData {
    private String levelId ;
    private String levelName;
    private Integer level;
    private Integer requiredExperience;
    private String privilegeDescription;
}
