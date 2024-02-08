package priv.backend.mapper;

import priv.backend.domain.PathConfig;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 角色权限配置DAO
 * @CreateDate :  2024-02-04 15:16
 */
public interface PathConfigMapper {
    /* TODO: Written by - Han Yongding 2024/02/04 从数据库中查询角色名称以及对应权限 */
    List<PathConfig> getAllPathConfigs() ;
}
