package priv.backend.domain.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 作用
 * @CreateDate :  2024-03-27 8:37
 */
@Data
@AllArgsConstructor
public class LoginConfirmVO {
    String accountOrEmail ;
    @Length(min = 6, max = 6)
    String code ;
}
