package priv.backend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import priv.backend.domain.dto.TypesWorkExperience;
import priv.backend.domain.vo.request.RestTypesWorkExperienceVO;
import priv.backend.domain.vo.response.RespTypesWorkExperienceVO;
import priv.backend.mapper.TypesWorkExperienceMapper;
import priv.backend.service.TypesWorkExperienceService;
import priv.backend.util.CurrentUtils;
import priv.backend.util.PageUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 工作类型业务层实现类
 * @CreateDate :  2024-02-28 12:32
 */
@Service
public class TypesWorkExperienceServiceImpl implements TypesWorkExperienceService {
    /* TODO: Written by - Han Yongding 2024/02/28 注入工作类型DAO */
    @Resource
    private TypesWorkExperienceMapper mapper ;

    /** TODO: Written by - Han Yongding 2024/02/28 获取所有工作类型 */
    @Override
    public Object getAllTypesWorkExperience(int pageNum, int pageSize, boolean isItPaginated) {
        /* TODO: Written by - Han Yongding 2024/02/28 判断是否需要分页 */
        if (isItPaginated) {
            Page<TypesWorkExperience> page = new Page<>(pageNum, pageSize);
            Page<TypesWorkExperience> returnPage = mapper.getPageTypesWorkExperience(page);

            /* TODO: Written by - Han Yongding 2024/02/28 拷贝属性 */
            List<RespTypesWorkExperienceVO> list = returnPage
                    .getRecords()
                    .stream()
                    .map(p -> p.asViewObject(RespTypesWorkExperienceVO.class))
                    .toList();

            return PageUtils.convertToPage(returnPage, list);
        }
        /* TODO: Written by - Han Yongding 2024/02/28 不分页直接返回 */
        return mapper.getAllTypesWorkExperience() ;
    }

    /** TODO: Written by - Han Yongding 2024/02/28 新增工作类型 */

    @Override
    public String insertTypesWorkExperience(RestTypesWorkExperienceVO vo) {
        if (vo == null) {
            return "数据不能为空，请稍后重试";
        }

        /* TODO: Written by - Han Yongding 2024/02/28 拷贝对象 */
        TypesWorkExperience typesWorkExperience = vo.asViewObject(TypesWorkExperience.class) ;
        /* TODO: Written by - Han Yongding 2024/02/28 初始化 */
        /* TODO: Written by - Han Yongding 2024/02/28 获取系统当前时间 */
        typesWorkExperience.setCreateTime(CurrentUtils.getTheCurrentSystemTime()) ;

        /* TODO: Written by - Han Yongding 2024/02/28 新增 */
        if(CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.insert(typesWorkExperience))) {
            return "新增失败，请稍后重试";
        }

        /* TODO: Written by - Han Yongding 2024/02/28 新增成功 */
        return null;
    }

    /** TODO: Written by - Han Yongding 2024/02/28 修改工作类型 */

    @Override
    public String updateTypesWorkExperience(RestTypesWorkExperienceVO vo) {
        if (vo == null) {
            return "数据不能为空，请稍后重试";
        }

        /* TODO: Written by - Han Yongding 2024/02/28 根据id修改 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.updateById(vo.asViewObject(TypesWorkExperience.class)))) {
            return "修改失败，请稍后重试";
        }

        /* TODO: Written by - Han Yongding 2024/02/28 修改成功 */
        return null;
    }

    /** TODO: Written by - Han Yongding 2024/02/28 根据ID删除工作类型 */

    @Override
    public String deleteTypesWorkExperience(String typeId) {
        TypesWorkExperience vo = new TypesWorkExperience() ;
        vo.setTypeId(typeId) ;
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.deleteById(vo))) {
            return "删除失败，请稍候再试" ;
        }

        /* TODO: Written by - Han Yongding 2024/02/28 删除成功 */
        return null;
    }
}
