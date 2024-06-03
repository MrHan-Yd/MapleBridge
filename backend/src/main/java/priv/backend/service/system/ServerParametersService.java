package priv.backend.service.system;

import priv.backend.domain.vo.response.RespServerParametersVO;
import priv.backend.domain.vo.response.RespServiceInformationVO;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 服务器参数业务层接口
 * @CreateDate :  2024-05-26 21:50
 */
public interface ServerParametersService {

    /* TODO: Written by - Han Yongding 2024/05/26 查询服务器参数 */
    RespServerParametersVO getServerParameters() ;

    /* TODO: Written by - Han Yongding 2024/06/02 查询服务器信息 */
    RespServiceInformationVO getServiceInformation() ;
}
