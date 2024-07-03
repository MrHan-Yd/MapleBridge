package priv.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import priv.backend.domain.PageBean;
import priv.backend.domain.dto.SensitiveWords;
import priv.backend.domain.vo.request.RestSensitiveWordsVO;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 关键词表业务层
 * @CreateDate :  2024-06-30 13:38
 */
public interface SensitiveWordsService extends IService <SensitiveWords> {

    /* TODO: Written by - Han Yongding 2024/06/30 分页查询所有关键词 */
    Page<SensitiveWords> getAll(PageBean pageBean);

    /* TODO: Written by - Han Yongding 2024/06/30 不分页只查询关键词字段 */
    Map<String, String> getWords();

    /* TODO: Written by - Han Yongding 2024/06/30 新增关键词 */
    String add(RestSensitiveWordsVO vo) ;

    /* TODO: Written by - Han Yongding 2024/06/30 删除关键词 */
    String delete(String id) ;

    /* TODO: Written by - Han Yongding 2024/07/01 批量删除关键字 */
    String deleteBatch(List<String> ids) ;
}
