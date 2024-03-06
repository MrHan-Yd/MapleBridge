package priv.backend.service.system.Impl;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import priv.backend.domain.PathConfig;
import priv.backend.mapper.PathConfigMapper;
import priv.backend.service.system.PathConfigService;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 角色权限配置业务层实现类
 * @CreateDate :  2024-02-03 22:22
 */
@Service
public class PathConfigServiceImpl implements PathConfigService {
    /* TODO: Written by - Han Yongding 2024/02/04 注入权限配置DAO */
    @Resource
    private PathConfigMapper mapper ;

    /* TODO: Written by - Han Yongding 2024/03/04 从数据库中获取路径配置的方法 */
    @Override
    public List<PathConfig> getAllPathConfigs() {
        return mapper.getAllPathConfigs();
    }
}
