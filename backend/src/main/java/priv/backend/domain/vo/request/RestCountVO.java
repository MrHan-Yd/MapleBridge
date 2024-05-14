package priv.backend.domain.vo.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import priv.backend.domain.BaseData;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 计数类
 * @CreateDate :  2024-04-15 9:39
 */
@Data
public class RestCountVO implements BaseData {
    @NotNull
    private String id ;
    @NotNull
    private String userId ;
    private Double version ;
    private String commentId ;
    @Pattern(regexp = "like|unlike")
    private String type ;
}
