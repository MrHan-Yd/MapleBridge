package priv.backend.domain.es.dto;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : es 点赞表
 * @CreateDate :  2024-03-31 21:29
 */
@Data
public class ESLike {
    @Field(type = FieldType.Text)
    private String likeId ;
    @Field(type = FieldType.Text)
    private String userId ;
    @Field(type = FieldType.Text)
    private String postId ;
    @Field(type = FieldType.Text)
    private String commentId ;
    @Field(type = FieldType.Date)
    private Date timestamp ;
}
