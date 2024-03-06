package priv.backend.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 作用
 * @CreateDate :  2024-01-09 21:59
 */
@Getter
@Document(collection = "mycollection")
public class MyEntity {
    @Id
    @MongoId
    private String id ;
    private String name ;

    public MyEntity() {
    }

    public MyEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

}
