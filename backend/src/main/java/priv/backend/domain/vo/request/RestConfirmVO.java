package priv.backend.domain.vo.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 接收前端重置密码请求，验证电子邮件实体类
 * @CreateDate :  2024-02-16 20:57
 */
@Data
@AllArgsConstructor
public class RestConfirmVO {
    @Email
    String email ;
    @Length(min = 6, max = 6)
    String code ;
}
