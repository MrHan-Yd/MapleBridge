package priv.backend.domain.mongo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 帖子类型和数量
 * @CreateDate :  2024-05-07 15:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MongoType {
    private String typeId ;
    private Integer count ;

}
