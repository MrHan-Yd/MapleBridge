package priv.backend.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 用户偏好表
 * @CreateDate :  2024-05-14 21:41
 */
@Data
@TableName("user_preferences")
public class UserPreferences {
    @TableId(type = IdType.ASSIGN_ID)
    private String preferenceId ;
    @TableField("user_id")
    private String userId ;
    @TableField("interests_1")
    private String interests1 ;
    @TableField("interests_2")
    private String interests2 ;
    @TableField("last_updated")
    private Timestamp lastUpdated ;
}
