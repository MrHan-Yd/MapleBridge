package priv.backend.domain.mongo.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 用户偏好文档
 * @CreateDate :  2024-05-07 19:21
 */
@Data
@Document
public class MongoUserPreferences {

    @Id
    private String userId ;
    private List<MongoType> typeList ;
}
