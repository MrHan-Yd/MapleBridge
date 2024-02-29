package priv.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import priv.backend.domain.dto.TypesWorkExperience;
import priv.backend.domain.vo.response.RespTypesWorkExperienceVO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 工作类型Mapper
 * @CreateDate :  2024-02-28 12:33
 */
public interface TypesWorkExperienceMapper extends BaseMapper<TypesWorkExperience> {
    /* TODO: Written by - Han Yongding 2024/02/28 分页查询工作类型 */
    Page<TypesWorkExperience> getPageTypesWorkExperience(Page<TypesWorkExperience> page) ;

    /* TODO: Written by - Han Yongding 2024/01/30 查询所有工作类型 */
    List<RespTypesWorkExperienceVO> getAllTypesWorkExperience() ;
}
