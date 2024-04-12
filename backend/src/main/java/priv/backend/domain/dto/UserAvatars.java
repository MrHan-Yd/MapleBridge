package priv.backend.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 用户头像路径DTO
 * @CreateDate :  2024-04-06 12:45
 */
@Data
@TableName("user_avatars")
public class UserAvatars {
    @TableId(type = IdType.ASSIGN_ID)
    private String avatarsId ;
    @TableField("user_id")
    private String userId ;
    @TableField("avatar_path")
    private String avatarPath ;
    @TableField("update_time")
    private Date updateTime ;
    @TableField("file_name")
    private String fileName ;
    @TableField("file_size")
    private String fileSize ;
}
