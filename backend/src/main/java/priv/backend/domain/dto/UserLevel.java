package priv.backend.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import priv.backend.domain.BaseData;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 用户等级表DTO
 * @CreateDate :  2024-02-06 17:24
 */
@Data
@TableName("user_level")
public class UserLevel implements BaseData {
    @TableId(type = IdType.ASSIGN_ID)
    private String levelId ;
    @TableField("level_name")
    private String levelName;
    @TableField("level")
    private Integer level;
    @TableField("required_experience")
    private Integer requiredExperience;
    @TableField("privilege_description")
    private String privilegeDescription;
}
