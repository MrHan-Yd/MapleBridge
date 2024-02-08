package priv.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import priv.backend.domain.vo.request.RestStatusUserVO;
import priv.backend.domain.vo.request.RestUserStateVO;
import priv.backend.domain.vo.response.RespStatusUserVO;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 用户状态业务层
 * @CreateDate :  2024-02-08 11:30
 */
public interface StatusUserService {
    /* TODO: Written by - Han Yongding 2024/02/08 新增用户状态 */
    String insertStatusUser(RestStatusUserVO vo) ;

    /* TODO: Written by - Han Yongding 2024/02/08 分页查询所有用户状态 */
    Page<RespStatusUserVO> getAllStatusUsers(int pageNum, int pageSize) ;

    /* TODO: Written by - Han Yongding 2024/02/08 根据ID修改用户状态 */
    String updateStatusUserStateById(RestUserStateVO vo) ;

    /* TODO: Written by - Han Yongding 2024/02/08 删除用户状态，伪删除 */
    String deleteStatusUserByStatusId(String statusId) ;
}
