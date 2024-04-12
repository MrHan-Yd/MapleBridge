package priv.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import priv.backend.domain.dto.User;
import priv.backend.domain.es.dto.ESClientUser;
import priv.backend.domain.vo.request.RestClientUserVO;
import priv.backend.domain.vo.request.RestUserVO;
import priv.backend.domain.vo.response.RespUserVO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

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

    /* TODO: Written by - Han Yongding 2024/04/07 查询所有用户信息，同步es使用 */
    List<ESClientUser> getClientUserES() ;

    /* TODO: Written by - Han Yongding 2024/04/07 根据用户ID，查询对应用户信息 */
    Optional<ESClientUser> getClientUserById(String userId) ;

    /* TODO: Written by - Han Yongding 2024/04/11 客户端修改用户信息 */
    String updateClientUserById(RestClientUserVO vo) ;

    /* TODO: Written by - Han Yongding 2024/04/11 根据用户ID查询用户信息，同步ES使用 */
    ESClientUser getClientUserByUserId(String userId) ;
}
