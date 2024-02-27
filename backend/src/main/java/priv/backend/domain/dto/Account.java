package priv.backend.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import priv.backend.domain.BaseData;
import priv.backend.domain.vo.response.RespAuthorizeVO;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 账号登录使用DTO
 * @CreateDate :  2024-02-16 14:06
 */
@Data
public class Account implements BaseData {
    private String id ;
    private String account ;
    private String password ;
    private String email ;
    private String roleId ;
}
