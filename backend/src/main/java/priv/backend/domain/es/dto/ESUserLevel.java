package priv.backend.domain.es.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : ES使用的用户等级DTO
 * @CreateDate :  2024-04-12 0:20
 */
@Data
public class ESUserLevel {
    private String levelId ;
    private String levelName;
    private Integer level;
    private Integer requiredExperience;
    private String privilegeDescription;
}
