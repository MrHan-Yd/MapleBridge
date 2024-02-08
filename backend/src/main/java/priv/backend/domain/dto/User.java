package priv.backend.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 用户表实体类
 * @CreateDate :  2024-02-04 20:06
 */
@Data
@TableName("user")
public class User {
    @TableId(type = IdType.ASSIGN_ID)
    private String id ;
    @TableField("account")
    private String account ;
    @TableField("password")
    private String password ;
    @TableField("email")
    private String email ;
    @TableField("nickname")
    private String nickname ;
    @TableField("gender")
    private String gender ;
    @TableField("birthday")
    private Timestamp birthday ;
    @TableField("avatars_id")
    private String avatarsId ;
    @TableField("profile_id")
    private String profileId ;
    @TableField("level_id")
    private String levelId ;
    @TableField("experience")
    private Integer experience ;
    @TableField("register_time")
    private Timestamp registerTime ;
    @TableField("status_id")
    private String statusId ;
    @TableField("create_id")
    private String createId ;
    @TableField("create_time")
    private Timestamp createTime ;
    @TableField("update_id")
    private String updateId ;
    @TableField("update_time")
    private Timestamp updateTime ;

}
