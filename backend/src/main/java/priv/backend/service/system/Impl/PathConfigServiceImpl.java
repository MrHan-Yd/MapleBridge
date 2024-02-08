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
    // 从数据库中获取路径配置的方法
    @Override
    public List<PathConfig> getAllPathConfigs() {
        // 实际项目中需要根据数据库查询返回相应的路径配置信息
        // 这里只是一个简单的模拟
//        return List.of(
//                new PathConfig("/admin/**", "ADMIN"),
//                new PathConfig("/user/**", "USER")
//        );
        return mapper.getAllPathConfigs();
    }
}
