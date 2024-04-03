package priv.backend.domain.vo.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import priv.backend.domain.BaseData;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 作用
 * @CreateDate :  2024-04-03 16:21
 */
@Data
public class RestPostsVO implements BaseData {
    private String postId ;
    private String userId ;
    private String topic ;
    private String content ;
    private String typeId ;
    private List<MultipartFile> list ;
}
