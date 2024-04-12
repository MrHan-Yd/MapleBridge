package priv.backend.initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import priv.backend.domain.es.dto.ESClientUser;
import priv.backend.domain.es.vo.RespESClientUser;
import priv.backend.repository.ESClientUserRepository;
import priv.backend.service.impl.UserServiceImpl;
import priv.backend.util.CurrentUtils;
import priv.backend.util.LogUtils;
import priv.backend.util.TimeUtils;
import priv.backend.util.UploadUtils;

import java.util.List;
import java.util.stream.StreamSupport;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 客户端查询用户数据使用
 * @CreateDate :  2024-04-07 10:12
 */
@Component
public class ClientUserElasticsearchInitializer implements CommandLineRunner {

    /* TODO: Written by - Han Yongding 2024/04/07 注入用户业务层实现类 */
    private final UserServiceImpl userService ;

    /* TODO: Written by - Han Yongding 2024/04/07 注入ES DAO层 */
    private final ESClientUserRepository esClientUserRepository ;

    /* TODO: Written by - Han Yongding 2024/04/12 注入上载工具类 */
    private final UploadUtils uploadUtils ;

    @Autowired
    public ClientUserElasticsearchInitializer(UserServiceImpl userService,
                                              ESClientUserRepository esClientUserRepository,
                                              UploadUtils uploadUtils) {
        this.userService = userService ;
        this.esClientUserRepository = esClientUserRepository ;
        this.uploadUtils = uploadUtils ;
    }

    /* TODO: Written by - Han Yongding 2024/04/07 程序启动初始化ClientUser到ES中 */
    @Override
    public void run(String... args) {
        LogUtils.info(this.getClass(),"客户端用户数据同步Elasticsearch任务开始");

        /* TODO: Written by - Han Yongding 2024/04/07 记录开始时间 */
        TimeUtils.start() ;

        LogUtils.info(this.getClass(),"正在获取数据");
        List<ESClientUser> list = userService.getClientUserES()
                .stream()
                .peek(l -> {
                    if (l.getAvatars() != null) {
                        String string = uploadUtils.generateAccessPath(l.getId(), "avatars");
                        l.getAvatars().setAvatarPath(string);
                    }
                }).toList();

        LogUtils.info(this.getClass(),"数据已成功获取");

        LogUtils.info(this.getClass(),"正在同步");
        Iterable<ESClientUser> esClientUsers = esClientUserRepository.saveAll(list);

        /* TODO: Written by - Han Yongding 2024/04/07 长度不相等则初始化失败 */
        if (CurrentUtils.sizeWhetherUnequal(
                list.size(),
                StreamSupport.stream(esClientUsers.spliterator(), false)
                        .toList().size())) {
            LogUtils.info(this.getClass(),"数据同步失败，原因：数据丢失或者部分数据丢失");
            return ;
        }
        LogUtils.info(this.getClass(),"客户端用户数据数据已同步至Elasticsearch");
        /* TODO: Written by - Han Yongding 2024/04/01 计算初始化耗时 */
        LogUtils.info(this.getClass(),"同步耗时" + TimeUtils.getCurrentTime(TimeUtils.getStartTime()));
        LogUtils.info(this.getClass(),"客户端用户数据同步Elasticsearch任务结束");
    }
}
