package priv.backend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import priv.backend.domain.PageBean;
import priv.backend.domain.RestBean;
import priv.backend.domain.dto.TypesPost;
import priv.backend.domain.vo.request.RestPostVO;
import priv.backend.domain.vo.request.RestTypesPostVO;
import priv.backend.domain.vo.response.RespTypesPostSelectVO;
import priv.backend.domain.vo.response.RespTypesPostVO;
import priv.backend.mapper.TypesPostMapper;
import priv.backend.service.TypesPostService;
import priv.backend.util.CurrentUtils;
import priv.backend.util.PageUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 帖子类型业务层实现类
 * @CreateDate :  2024-03-08 12:40
 */
@Service
public class TypesPostServiceImpl implements TypesPostService {
    /* TODO: Written by - Han Yongding 2024/03/08 注入帖子类型DAO */
    @Resource
    private TypesPostMapper mapper;

    /** TODO: Written by - Han Yongding 2024/03/08 查询帖子类型，分页或不分页 */
    @Override
    public Object getPostTypes(PageBean pageBean) {
        /* TODO: Written by - Han Yongding 2024/03/08 分页 */
        if (pageBean.getIsItPaginated()) {
            /* TODO: Written by - Han Yongding 2024/03/08 配置分页 */
            Page<TypesPost> page = new Page<>(pageBean.getPageNum(), pageBean.getPageSize());
            /* TODO: Written by - Han Yongding 2024/03/04 获取数据 */
            Page<TypesPost> resp = mapper.getPageTypesPost(page);

            /* TODO: Written by - Han Yongding 2024/03/08 拷贝属性 */
            List<RespTypesPostVO> list = resp
                    .getRecords()
                    .stream()
                    .map(p -> p.asViewObject(RespTypesPostVO.class))
                    .toList();

            return PageUtils.convertToPage(resp, list) ;
        }
        /* TODO: Written by - Han Yongding 2024/03/08 不分页 */
        return mapper.getAllTypesPost() ;
    }
    /* TODO: Written by - Han Yongding 2024/03/08 查询帖子类型，不分页 */
    @Override
    public Object getPostTypes() {
        return mapper.getAllTypesPost() ;
    }

    /** TODO: Written by - Han Yongding 2024/03/08 新增帖子类型 */
    @Override
    public String insertTypesPost(RestTypesPostVO vo) {
        if (vo == null) {
            return "数据为空，请稍后再试" ;
        }

        /* TODO: Written by - Han Yongding 2024/03/08 初始化 */
        TypesPost typesPost = vo.asViewObject(TypesPost.class);
        typesPost.setCreateTime(CurrentUtils.getTheCurrentSystemTime());

        /* TODO: Written by - Han Yongding 2024/03/08 新增失败 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.insert(typesPost))) {
            return "新增失败，请稍后再试" ;
        }

        /* TODO: Written by - Han Yongding 2024/03/04 新增成功 */
        return null;
    }


    /* TODO: Written by - Han Yongding 2024/03/08 修改帖子类型 */
    @Override
    public String updateTypesPostByTypeId(RestTypesPostVO vo) {
        if (vo == null) {
            return "数据为空，请稍后再试" ;
        }

        /* TODO: Written by - Han Yongding 2024/03/30 修改时间 */
        TypesPost typesPost = vo.asViewObject(TypesPost.class) ;
        typesPost.setUpdateTime(CurrentUtils.getTheCurrentSystemTime()) ;
        /* TODO: Written by - Han Yongding 2024/03/08 修改失败 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.updateById(typesPost))) {
            return "修改失败，请稍候再试" ;
        }

        /* TODO: Written by - Han Yongding 2024/03/08 修改成功 */
        return null;
    }

    /* TODO: Written by - Han Yongding 2024/03/08 删除帖子类型 */

    @Override
    public String deleteTypesPostByTypeId(String typeId) {

        if (typeId == null) {
            return "唯一标识为空，请稍候再试" ;
        }

        /* TODO: Written by - Han Yongding 2024/03/08 删除失败 */
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.deleteById(typeId))) {
            return "删除失败，请稍后再试" ;
        }

        /* TODO: Written by - Han Yongding 2024/03/08 删除成功 */
        return null;
    }
}
