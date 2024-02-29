package priv.backend.service;

import priv.backend.domain.vo.request.RestTypesWorkExperienceVO;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 工作类型业务层
 * @CreateDate :  2024-02-28 12:31
 */
public interface TypesWorkExperienceService {

    /* TODO: Written by - Han Yongding 2024/02/28 获取所有工作类型 */
    Object getAllTypesWorkExperience(int pageNum, int pageSize, boolean isItPaginated);

    /* TODO: Written by - Han Yongding 2024/02/28 新增工作类型 */
    String insertTypesWorkExperience(RestTypesWorkExperienceVO vo) ;

    /* TODO: Written by - Han Yongding 2024/02/28 修改工作类型 */
    String updateTypesWorkExperience(RestTypesWorkExperienceVO vo) ;

    /* TODO: Written by - Han Yongding 2024/02/28 根据ID删除工作类型 */
    String deleteTypesWorkExperience(String typeId) ;
}
