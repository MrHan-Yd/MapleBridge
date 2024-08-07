package priv.backend.domain.es.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : es 评论表
 * @CreateDate :  2024-03-31 21:22
 */
@Data
public class ESComment {
    private String id ;
    private String postId ;
    private ESUser user ;
    private String content ;
    private Date timestamp ;
    private String likeCount ;
    private List<ESLike> likes ;
    private String commentId;
    private String replyId ;
    private List<ESComment> subComments ;
    private double version ;
}
