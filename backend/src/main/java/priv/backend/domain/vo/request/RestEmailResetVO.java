package priv.backend.domain.vo.request;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 忘记密码重置实体类
 * @CreateDate :  2024-02-27 22:25
 */
@Data
public class RestEmailResetVO {
    @Email
    String email ;
    @Length(min = 6, max = 6)
    String code ;
    @Length(min = 6, max = 20)
    String password ;
}
