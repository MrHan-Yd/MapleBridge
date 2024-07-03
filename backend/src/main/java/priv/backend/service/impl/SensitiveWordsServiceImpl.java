package priv.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import priv.backend.domain.PageBean;
import priv.backend.domain.dto.SensitiveWords;
import priv.backend.domain.vo.request.RestSensitiveWordsVO;
import priv.backend.enumeration.RespMessageEnum;
import priv.backend.mapper.SensitiveWordsMapper;
import priv.backend.service.SensitiveWordsService;
import priv.backend.service.system.SensitiveWordService;
import priv.backend.util.TimeUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 敏感词表业务层实现类
 * @CreateDate :  2024-06-30 13:38
 */
@Service
public class SensitiveWordsServiceImpl extends ServiceImpl<SensitiveWordsMapper, SensitiveWords> implements SensitiveWordsService {

    /* TODO: Written by - Han Yongding 2024/06/30 注入mapper接口，实现业务逻辑 */
    @Resource
    private SensitiveWordsMapper mapper;

    /*
     * 使用 @Lazy 注解，延迟加载 sensitiveWordService，以解决循环依赖问题。
     * 当 Spring IOC 容器创建该 bean 时，不会立即实例化 sensitiveWordService。
     * 只有在第一次访问 sensitiveWordService 时才会进行实例化。
     */
    @Resource
    @Lazy
    private SensitiveWordService sensitiveWordService ;
    @Override
    public Page<SensitiveWords> getAll(PageBean pageBean) {
        Page<SensitiveWords> page = new Page<>(pageBean.getPageNum(), pageBean.getPageSize());
        return mapper.selectPage(page, null);
    }

    @Override
    public String add(RestSensitiveWordsVO vo) {
        if (ObjectUtils.isEmpty(vo)) {
            return RespMessageEnum.PARAM_NOT_NULL.getMessage();
        }

        String word = vo.getWord().toUpperCase() ;
        /* TODO: Written by - Han Yongding 2024/06/30 关键词存在 */
        if (this.ExistingKeywords(word)) {
            return RespMessageEnum.DATA_EXIST.getMessage();
        }

        /* TODO: Written by - Han Yongding 2024/06/30 创建时间 */
        SensitiveWords viewObject = vo.asViewObject(SensitiveWords.class);
        viewObject.setCreateTime(TimeUtils.getLocalDateTime());
        /* TODO: Written by - Han Yongding 2024/06/30 全部为大写 */
        viewObject.setWord(word);

        /* TODO: Written by - Han Yongding 2024/06/30 插入敏感词失败 */
        if (!this.save(viewObject)) {
            return RespMessageEnum.DATA_INSERT_FAILED.getMessage();
        }

        /* TODO: Written by - Han Yongding 2024/06/30 新增成功 */
        /* TODO: Written by - Han Yongding 2024/06/30 更新关键词 */
        sensitiveWordService.loadSensitiveWords() ;
        return null;
    }

    @Override
    public Map<String, String> getWords() {
        QueryWrapper<SensitiveWords> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("word");
        List<SensitiveWords> sensitiveWordsList = this.list(queryWrapper); // 查询所有敏感词

        if (CollectionUtils.isEmpty(sensitiveWordsList)) {
            /* TODO: Written by - Han Yongding 2024/06/30 没有查询到敏感词  */
            return null;
        }

        /* TODO: Written by - Han Yongding 2024/06/30 转为Map */
        return sensitiveWordsList.stream()
                .collect(Collectors.toMap(
                        SensitiveWords::getWord,
                        SensitiveWords::getWord));
    }

    @Override
    public String delete(String id) {
        if (ObjectUtils.isEmpty(id)) {
            return RespMessageEnum.PARAM_NOT_NULL.getMessage();
        }

        /* TODO: Written by - Han Yongding 2024/06/30 删除失败 */
        if (!this.removeById(id)) {
            return RespMessageEnum.DATA_DELETE_FAILED.getMessage();
        }

        /* TODO: Written by - Han Yongding 2024/06/30 更新关键词 */
        sensitiveWordService.loadSensitiveWords() ;
        /* TODO: Written by - Han Yongding 2024/06/30 删除成功 */
        return null;
    }

    /* TODO: Written by - Han Yongding 2024/06/30 根据关键词查询数据库中是否存在 */
    private boolean ExistingKeywords(String word) {
        QueryWrapper<SensitiveWords> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("word", word);
        return mapper.selectCount(queryWrapper) > 0 ;
    }

    /* TODO: Written by - Han Yongding 2024/07/01 批量删除事务，要么都成功，要么回滚 */
    @Resource
    private TransactionTemplate deleteBatchTransactionTemplate ;

    @Override
    public String deleteBatch(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return RespMessageEnum.PARAM_NOT_NULL.getMessage();
        }

        return deleteBatchTransactionTemplate.execute(status -> {
            if (mapper.deleteBatchIds(ids) <= 0) {
                /* 回滚 */
                status.setRollbackOnly();
                return RespMessageEnum.DATA_DELETE_FAILED.getMessage();
            }

            /* TODO: Written by - Han Yongding 2024/06/30 更新关键词 */
            sensitiveWordService.loadSensitiveWords() ;
            /* TODO: Written by - Han Yongding 2024/07/01 业务结束，删除成功 */
            return null;
        });
    }
}
