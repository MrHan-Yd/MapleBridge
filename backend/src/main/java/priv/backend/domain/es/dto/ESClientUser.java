package priv.backend.domain.es.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import priv.backend.domain.BaseData;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 客户端ES
 * @CreateDate :  2024-04-07 8:52
 */
@Data
@Document(indexName = "maple_bridge_user")
public class ESClientUser implements BaseData {
    @Id
    private String id ;
    @Field(type = FieldType.Text)
    private String nickname ;
    @Field(type = FieldType.Text)
    private String gender ;
    @Field(type = FieldType.Date)
    private Date birthday ;
    @Field(type = FieldType.Date)
    private Date updateTime ;
    @Field(type = FieldType.Integer)
    private Integer experience ;
    @Field(type = FieldType.Nested, includeInParent = true)
    private ESUserAvatars avatars ;
    @Field(type = FieldType.Nested, includeInParent = true)
    private ESUserProfile userProfile ;
    @Field(type = FieldType.Nested, includeInParent = true)
    private ESUserLevel level ;
    @Field(type = FieldType.Nested, includeInParent = true)
    private ESProfile profile ;
}
