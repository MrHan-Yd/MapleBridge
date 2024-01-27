package priv.backend.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import priv.backend.domain.BaseData;

import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 角色状态表
 * @CreateDate :  2024-01-22 16:26
 */
@Data
@TableName("status_role")
public class StatusRole implements BaseData {
    /* TODO: Written by - Han Yongding 2024/01/22 雪花算法生成ID */
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
