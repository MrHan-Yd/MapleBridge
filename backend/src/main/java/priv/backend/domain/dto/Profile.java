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
 * @Description : 个人信息表
 * @CreateDate :  2024-04-06 12:54
 */
@Data
@TableName("profile")
@AllArgsConstructor
public class Profile {
    @TableId(type = IdType.ASSIGN_ID)
    private String profileId ;
    @TableField("user_id")
    private String userId ;
    @TableField("last_name")
    private String lastName ;
    @TableField("first_name")
    private String firstName ;
    @TableField("region")
    private String region ;
    @TableField("location")
    private String location ;
    @TableField("graduation_department")
    private String graduationDepartment ;
    @TableField("major")
    private String major ;
    @TableField("graduation_year")
    private String graduationYear ;
    @TableField("update_time")
    private Date update_time ;
}
