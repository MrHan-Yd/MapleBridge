package priv.backend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import priv.backend.domain.dto.StatusUser;
import priv.backend.domain.vo.request.RestStatusUserVO;
import priv.backend.domain.vo.request.RestUserStateVO;
import priv.backend.domain.vo.response.RespStatusUserVO;
import priv.backend.enumeration.StatusEnum;
import priv.backend.mapper.StatusUserMapper;
import priv.backend.service.StatusUserService;
import priv.backend.util.CurrentUtils;
import priv.backend.util.PageUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 用户状态业务层实现类
 * @CreateDate :  2024-02-08 11:30
 */
@Service
public class StatusUserServiceImpl implements StatusUserService {

    /* TODO: Written by - Han Yongding 2024/02/08 用户状态DAO注入 */
    @Resource
    private StatusUserMapper mapper;

    /** TODO: Written by - Han Yongding 2024/02/08 新增用户状态 */
    @Override
    public String insertStatusUser(RestStatusUserVO vo) {
        if (vo == null) {
            return "数据不能为空，请重试";
        }
        /* TODO: Written by - Han Yongding 2024/03/01 初始化 */
        StatusUser statusUser = vo.asViewObject(StatusUser.class);
        statusUser.setCreateTime(CurrentUtils.getTheCurrentSystemTime());
        statusUser.setState(StatusEnum.NORMAL.STATE);
        if (CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.insert(statusUser))) {
            return "添加失败，请重试";
        }
        return null;
    }

    /** TODO: Written by - Han Yongding 2024/02/08 查询所有用户状态 */
    @Override
    public Page<RespStatusUserVO> getAllStatusUsers(int pageNum, int pageSize) {
        Page<StatusUser> page = new Page<>(pageNum, pageSize);
        Page<StatusUser> statusUserPage = mapper.getAllStatusUsers(page);

        /* TODO: Written by - Han Yongding 2024/02/08 从数据库中查询后转为响应对象返回 */
        List<RespStatusUserVO> list = statusUserPage
                .getRecords()
                .stream()
                .map(p -> p.asViewObject(RespStatusUserVO.class))
                .toList();

        /* TODO: Written by - Han Yongding 2024/02/08 将分页数据和数据处理好后返回 */
        return PageUtils.convertToPage(statusUserPage, list);
    }

    /** TODO: Written by - Han Yongding 2024/02/08 根据ID修改用户状态 */
    @Override
    public String updateStatusUserStateById(RestUserStateVO vo) {

        /* TODO: Written by - Han Yongding 2024/02/08 数据为空 */
        if (vo == null) {
            return "数据错误，请重试" ;
        }

        /* TODO: Written by - Han Yongding 2024/03/30 修改时间 */
        StatusUser viewObject = vo.asViewObject(StatusUser.class);
        viewObject.setUpdateTime(CurrentUtils.getTheCurrentSystemTime());


        /* TODO: Written by - Han Yongding 2024/02/08 修改失败 */
        if(CurrentUtils.isEmptyByDtoInsertOrUpdate(mapper.updateById(viewObject))) {
            return "修改失败，请稍后重试" ;
        }
        /* TODO: Written by - Han Yongding 2024/02/08 修改成功 */
        return null ;
    }

    /** TODO: Written by - Han Yongding 2024/02/08 删除用户状态，伪删除 */
    @Override
    public String deleteStatusUserByStatusId(String statusId) {
        /* TODO: Written by - Han Yongding 2024/02/08 数据为空 */
        if (statusId == null) {
            return "数据错误，请重试" ;
        }

        StatusUser statusUser = new StatusUser() ;
        statusUser.setStatusId(statusId) ;
        statusUser.setState("2") ;
        /* TODO: Written by - Han Yongding 2024/02/08 修改失败 */
        if(mapper.updateById(statusUser) < 1) {
            return "修改失败，请稍后重试" ;
        }

        /* TODO: Written by - Han Yongding 2024/02/08 修改成功 */
        return null ;
    }

}
