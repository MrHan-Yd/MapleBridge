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
 * @Description : 工作类型DTO
 * @CreateDate :  2024-02-28 12:28
 */
@Data
@TableName("types_work_experience")
public class TypesWorkExperience implements BaseData {
    @TableId(type = IdType.ASSIGN_ID)
    private String typeId ;
    @TableField("type_name")
    private String typeName ;
    @TableField("description")
    private String description ;
    @TableField("create_id")
    private String createId ;
    @TableField("create_time")
    private Timestamp createTime ;
    @TableField("update_id")
    private String updateId ;
    @TableField("update_time")
    private Timestamp updateTime ;
}
