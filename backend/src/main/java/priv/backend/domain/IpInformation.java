package priv.backend.domain;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : Ip信息
 * @CreateDate :  2024-06-08 22:57
 */
@Data
public class IpInformation implements BaseData {

    /* TODO: Written by - Han Yongding 2024/06/08 国家 */
    private String country ;

    /* TODO: Written by - Han Yongding 2024/06/08 省份 */
    private String province;

    /* TODO: Written by - Han Yongding 2024/06/08 地区 */
    private String region;

    /* TODO: Written by - Han Yongding 2024/06/08 网络 */
    private String network;
}
