package priv.backend.service.system;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.Getter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import priv.backend.service.impl.SensitiveWordsServiceImpl;
import priv.backend.util.CurrentUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 敏感词服务
 * @CreateDate :  2024-06-30 13:58
 */
@Service
public class SensitiveWordService {
    /* TODO: Written by - Han Yongding 2024/06/30 关键词集合 */
    @Getter
    private Map<String, String> sensitiveWords = new HashMap<>();

    @Resource
    private SensitiveWordsServiceImpl service ;

    /* TODO: Written by - Han Yongding 2024/06/30 初始化 */
    @PostConstruct
    public void init() {
        this.loadSensitiveWords();
    }

    /* TODO: Written by - Han Yongding 2024/06/30 从表中获取关键词 */
    public void loadSensitiveWords() {
        sensitiveWords = service.getWords() ;
    }

}
