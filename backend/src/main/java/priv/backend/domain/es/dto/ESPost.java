package priv.backend.domain.es.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import priv.backend.domain.BaseData;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : es 帖子表DTO
 * @CreateDate :  2024-03-31 21:04
 */
@Document(indexName = "maple_bridge_post")
@Data
//@JsonIgnoreProperties(ignoreUnknown = true) // Model中需要添加此行代码，否则检索时可能会报错
public class ESPost implements BaseData {
    @Id
    private String postId ;
    @Field(type = FieldType.Nested, includeInParent = true)
    private ESUser user ;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String topic ;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String content ;
    @Field(type = FieldType.Date)
    private Date timestamp ;
    @Field(type = FieldType.Nested, includeInParent = true)
    private ESPostType type ;
    @Field(type = FieldType.Nested, includeInParent = true, index = false)
    private List<ESFilePost> filePost ;
    @Field(type = FieldType.Text)
    private String likeCount ;
    @Field(type = FieldType.Text)
    private String commentCount ;
    @Field(type = FieldType.Nested, includeInParent = true, index = false)
    private List<ESComment> comment ;
    @Field(type = FieldType.Nested, includeInParent = true, index = false)
    private List<ESLike> like ;
    @Field(type = FieldType.Double)
    private double version ;
    @Field(type = FieldType.Text)
    private String views;
}
