package priv.backend.domain.es.vo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import priv.backend.domain.es.dto.ESComment;
import priv.backend.domain.es.dto.ESFilePost;
import priv.backend.domain.es.dto.ESLike;
import priv.backend.domain.es.dto.ESPostType;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : ES 响应前端获取post数据
 * @CreateDate :  2024-04-04 16:54
 */
@Data
public class RespESPost {
    private String postId ;
    private String userId ;
    private String nickname ;
    private String topic ;
    private String content ;
    private Date timestamp ;
    private ESPostType type ;
    private List<ESFilePost> filePost ;
    private String likeCount ;
    private String commentCount ;
    private List<ESComment> comment ;
    private List<ESLike> like ;
}
