package priv.backend.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import priv.backend.domain.BaseData;

import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 权限表
 * @CreateDate :  2024-01-27 12:49
 */
@Data
@TableName("permission")
public class Permission implements BaseData {
    @TableId(type = IdType.ASSIGN_ID)
    private String permissionId ;
    @TableField("permission_name")
    private String permissionName ;
    @TableField("permission_url")
    private String permissionUrl ;
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
