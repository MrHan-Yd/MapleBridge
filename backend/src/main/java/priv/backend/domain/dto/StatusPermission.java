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
 * @Description : 权限状态类DTO
 * @CreateDate :  2024-01-26 15:49
 */
@Data
@TableName("status_permission")
public class StatusPermission implements BaseData {
    @TableId(type = IdType.ASSIGN_ID)
    private String statusId ;
    @TableField("status_name")
    private String statusName ;
    @TableField("state")
    private String state ;
    @TableField("create_id")
    private String createId ;
    @TableField("create_time")
    private Timestamp createTime ;
    @TableField("update_id")
    private String updateId ;
    @TableField("update_time")
    private Timestamp updateTime ;
}
