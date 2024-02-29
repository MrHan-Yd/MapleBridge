package priv.backend.domain.vo.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 响应前端工作类型VO
 * @CreateDate :  2024-02-28 12:40
 */
@Data
public class RespTypesWorkExperienceVO {
    private String typeId ;
    private String typeName ;
    private String description ;
    private String createId ;
    private Timestamp createTime ;
    private String updateId ;
    private Timestamp updateTime ;
}
