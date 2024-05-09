package priv.backend.domain.mongo.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import priv.backend.domain.BaseData;
import priv.backend.domain.mongo.dto.MongoType;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 收集用户感兴趣的帖子类型
 * @CreateDate :  2024-05-07 15:28
 */
@Data
public class RestMongoPostTypeVO implements BaseData {
    @NotNull
    private String userId ;
    @NotNull
    private List<MongoType> typeList ;
}
