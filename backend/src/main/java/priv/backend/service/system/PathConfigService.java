package priv.backend.service.system;

import priv.backend.domain.PathConfig;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 角色权限配置业务层
 * @CreateDate :  2024-02-03 22:22
 */
public interface PathConfigService {
    /* TODO: Written by - Han Yongding 2024/02/03 获取路径配置的方法 */
    List<PathConfig> getAllPathConfigs() ;
}
