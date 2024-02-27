package priv.backend.domain.vo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 接收前端邮箱注册请求实体类
 * @CreateDate :  2024-02-16 20:39
 */
@Data
public class RestEmailRegisterVO {
    @Email
    @Length(min = 4)
    String email ;
    /** TODO: Written by - Han Yongding 2023/08/14 验证码长度限制，最长6，最短6，不符合要求为不合法 */
    @Length(max = 6, min = 6)
    String code ;
    @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$")
    @Length(min = 1, max = 10)
    String account ;
    @Length(min = 6, max = 20)
    String password ;
}
