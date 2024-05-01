package priv.backend.domain.es.dto;

import lombok.Data;
import priv.backend.domain.BaseData;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 帖子用户信息
 * @CreateDate :  2024-04-27 22:47
 */
@Data
public class ESUser implements BaseData {
    private String userId ;
    private String nickname ;
    private String path ;
    private String fileName ;
    private String level ;
    private String levelName ;
}
