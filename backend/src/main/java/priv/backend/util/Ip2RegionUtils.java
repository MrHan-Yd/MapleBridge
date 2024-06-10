package priv.backend.util;

import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import priv.backend.domain.IpInformation;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 离线IP定位框架工具类
 * @CreateDate :  2024-06-08 22:36
 */
@Component
public class Ip2RegionUtils {

    /**
     * 将整个库进行缓存到内存，基于这个库创建查询对象来实现基于文件的查询
     * @return 获取Searcher
     */
    private Searcher getSearcher() throws IOException {
        /* TODO: Written by - Han Yongding 2024/06/08 获取ip离线库资源 */
        byte[] bytes = Searcher.loadContentFromFile(ResourceUtils.getFile("classpath:ip/ip2region.xdb").getCanonicalPath()) ;
        return Searcher.newWithBuffer(bytes) ;
    }

    /**
     * 根据ip地址直接返回国家、省、城市信息
     * @param ip ip
     * @return 返回地址
     */
    public String getIpPosition(String ip) {
        Searcher searcher = null ;
        try {
            searcher = getSearcher() ;
            return searcher.search(ip) ;
        } catch (Exception e) {
            LogUtils.error(this.getClass(), String.format("ip转地址报错:%s",e.getMessage()));
            return null ;
        } finally {
            try {
                if (Objects.nonNull(searcher)){
                    searcher.close();
                }
            }catch (Exception e){
                LogUtils.error(this.getClass(), String.format("ip转地址报错:%s",e.getMessage()));
            }
        }
    }

    public IpInformation getIpInformation(String resolveLocation) {
        String[] str = resolveLocation.split("\\|") ;
        IpInformation ipInformation = new IpInformation() ;
        ipInformation.setCountry(this.isUnknown(str[0]));
        ipInformation.setProvince(this.isUnknown(str[2]));
        ipInformation.setRegion(this.isUnknown(str[3]));
        ipInformation.setNetwork(this.isUnknown(str[4]));
        return ipInformation ;
    }

    private String isUnknown(String str) {
        return this.isZero(str) ? "未知" : str ;
    }

    private boolean isZero(String str) {
        return "0".equals(str) ;
    }
}
