package priv.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import priv.backend.domain.dto.User;
import priv.backend.domain.vo.request.RestUserVO;
import priv.backend.domain.vo.response.RespUserVO;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 用户业务层
 * @CreateDate :  2024-02-06 22:54
 */
public interface UserService extends IService<User>, UserDetailsService {
    /* TODO: Written by - Han Yongding 2024/02/06 根据等级id查询是否正在使用 */
    boolean isUsingLevelId(String levelId) ;

    /* TODO: Written by - Han Yongding 2024/02/08 新增用户 */
    String insertUser(RestUserVO vo) ;

    /* TODO: Written by - Han Yongding 2024/02/12 更新用户信息 */
    String updateUser(RestUserVO vo) ;

    /* TODO: Written by - Han Yongding 2024/02/12 分页查询所有用户 */
    Page<RespUserVO> getAllUser(int pageNum, int pageSize) ;

    /* TODO: Written by - Han Yongding 2024/02/13 根据ID删除用户，伪删除 */
    String deleteUserById(String id) ;
}
