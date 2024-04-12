package priv.backend.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import priv.backend.domain.BaseData;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 用户表实体类DTO
 * @CreateDate :  2024-02-04 20:06
 */
@Data
@TableName("user")
@NoArgsConstructor
public class User implements BaseData {
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
    private Date birthday ;
    @TableField("avatars_id")
    private String avatarsId ;
    @TableField("profile_id")
    private String profileId ;
    @TableField("role_id")
    private String roleId ;
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
    private Date updateTime ;

    public User(String id,
                String nickname,
                String gender,
                Date birthday,
                String avatarsId,
                String profileId,
                String updateId,
                Date updateTime) {
        this.id = id ;
        this.nickname = nickname ;
        this.gender = gender ;
        this.birthday = birthday ;
        this.avatarsId = avatarsId ;
        this.profileId = profileId ;
        this.updateId = updateId ;
        this.updateTime = updateTime ;
    }

}
