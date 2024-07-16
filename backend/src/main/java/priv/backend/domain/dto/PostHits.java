package priv.backend.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import priv.backend.util.TimeUtils;

import java.time.LocalDateTime;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 帖子点击量表
 * @CreateDate :  2024-07-08 15:03
 */
@Data
@TableName("post_hits")
public class PostHits {

    @TableId("post_id")
    private String postId ;

    @TableField("hits")
    private String hits ;

    @TableField("version")
    private Double version ;

    @TableField("update_time")
    private LocalDateTime updateTime ;

    public PostHits(String postId) {
        this.postId = postId;
        this.updateTime = TimeUtils.getLocalDateTime() ;
    }

    public PostHits(String postId, String hits) {
        this.postId = postId;
        this.hits = hits;
        this.updateTime = TimeUtils.getLocalDateTime() ;
    }

    public PostHits(String postId, String hits, Double version) {
        this.postId = postId;
        this.hits = hits;
        this.version = version;
        this.updateTime = TimeUtils.getLocalDateTime() ;
    }
}
