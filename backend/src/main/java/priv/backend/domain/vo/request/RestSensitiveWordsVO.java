package priv.backend.domain.vo.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import priv.backend.domain.BaseData;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 接收前端敏感词请求参数
 * @CreateDate :  2024-06-30 13:42
 */
@Data
public class RestSensitiveWordsVO implements BaseData {
    private String id ;
    private String word;
    private String createId;
}
