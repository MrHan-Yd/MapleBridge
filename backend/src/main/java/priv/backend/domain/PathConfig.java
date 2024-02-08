package priv.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 角色权限配置
 * @CreateDate :  2024-02-03 21:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PathConfig {
    private String pathPattern ;
    private String roleName ;

}
