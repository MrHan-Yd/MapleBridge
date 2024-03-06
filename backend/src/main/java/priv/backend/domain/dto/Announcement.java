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
 * @Description : 公告DTO
 * @CreateDate :  2024-03-04 20:39
 */
@Data
@TableName("announcement")
public class Announcement implements BaseData {
    @TableId(type = IdType.ASSIGN_ID)
    private String id ;
    @TableField("title")
    private String title ;
    @TableField("content")
    private String content ;
    @TableField("publish_time")
    private Timestamp publishTime ;
    @TableField("publisher_id")
    private String publisherId ;
    @TableField("deadline")
    private Timestamp deadline ;
    @TableField("type_id")
    private String typeId ;
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
