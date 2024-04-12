package priv.backend.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 用户简介表
 * @CreateDate :  2024-04-06 12:40
 */
@Data
@TableName("user_profile")
@AllArgsConstructor
public class UserProfile {
    @TableId(type = IdType.ASSIGN_ID)
    private String profileId ;
    @TableField("bio")
    private String bio ;
    @TableField("update_time")
    private Date updateTime ;
}
