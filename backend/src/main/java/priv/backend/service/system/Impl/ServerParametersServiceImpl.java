package priv.backend.service.system.Impl;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import priv.backend.domain.ServerParameters;
import priv.backend.domain.ServiceInformation;
import priv.backend.domain.vo.response.RespServerParametersVO;
import priv.backend.domain.vo.response.RespServiceInformationVO;
import priv.backend.enumeration.LockEnum;
import priv.backend.repository.redis.ServerParametersRepository;
import priv.backend.repository.redis.ServiceInformationRepository;
import priv.backend.service.system.ServerParametersService;
import priv.backend.util.Const;
import priv.backend.util.LogUtils;
import priv.backend.util.SystemUtils;
import priv.backend.util.TimeUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : weiguang
 * @Description : 服务器参数业务层接口实现类
 * @CreateDate :  2024-05-26 21:50
 */
@Service
public class ServerParametersServiceImpl implements ServerParametersService {

    /* TODO: Written by - Han Yongding 2024/06/02 引入RedissonClient  */
    private final RedissonClient redissonClient ;

    /* TODO: Written by - Han Yongding 2024/06/02 引入服务器参数Redis数据层  */
    private final ServerParametersRepository serverParametersRepository;

    /* TODO: Written by - Han Yongding 2024/06/02 引入服务信息Redis数据层  */
    private final ServiceInformationRepository serviceInformationRepository;

    /* TODO: Written by - Han Yongding 2024/06/02 系统工具类 */
    private final SystemUtils systemUtils;

    @Autowired
    public ServerParametersServiceImpl(RedissonClient redissonClient,
                                       ServerParametersRepository serverParametersRepository,
                                       ServiceInformationRepository serviceInformationRepository,
                                       SystemUtils systemUtils) {
        this.redissonClient = redissonClient;
        this.serverParametersRepository = serverParametersRepository;
        this.serviceInformationRepository = serviceInformationRepository;
        this.systemUtils = systemUtils;
    }

    /* TODO: Written by - Han Yongding 2024/05/26 查询服务器参数 */
    @Override
    public RespServerParametersVO getServerParameters() {
        String key = Const.REDIS_DATA_KEY;

        /* 缓存中没有或者过期了 */
        if (!Boolean.TRUE.equals(serverParametersRepository.existsById(key)) || TimeUtils.isExpired(Objects.requireNonNull(serverParametersRepository.findById(key).get().getExpirationTime()))) {
            RLock lock = redissonClient.getLock(LockEnum.SERVER_PARAMETERS.lockName);
            try {
                /* 尝试获取锁，等待时间10秒，租约时间90秒, 因为获取系统参数方法等待一秒钟获取cpu的使用情况 */
                boolean lockAcquired = lock.tryLock(10, 90, TimeUnit.SECONDS);

                /* 拿到锁 */
                if (lockAcquired) {
                    try {
                        /* 缓存统建 */
                        ServerParameters serverParameters = SystemUtils.getServerParameters(key);
                        serverParametersRepository.save(serverParameters) ;


                        /* TODO: Written by - Han Yongding 2024/05/28 返回数据 */
                        return serverParameters.asViewObject(RespServerParametersVO.class);
                    } finally {
                        /* 释放锁 */
                        lock.unlock();
                    }
                } else {
                    /* 没有拿到锁，等待缓存重建完成 */
                    while (!Boolean.TRUE.equals(serverParametersRepository.existsById(key))) {
                        try {
                            /* TODO: Written by - Han Yongding 2024/05/28 等待一小段时间后重新检查缓存是否存在 */
                            TimeUnit.MILLISECONDS.sleep(500);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            LogUtils.error(this.getClass(), "服务器参数-线程等待缓存重建时被中断: " + e);
                            return null;
                        }
                    }

                    /* 没有拿到锁，返回缓存中的数据 */
                     Optional<ServerParameters> cachedData = serverParametersRepository.findById(key);
                    return cachedData.map(serverParameters -> serverParameters.asViewObject(RespServerParametersVO.class)).orElse(null);
                }
            } catch (InterruptedException e) {
                LogUtils.error(this.getClass(), "系统参数缓存重建，获取锁异常: " + e);
                /* TODO: Written by - Han Yongding 2024/05/28 恢复中断状态 */
                Thread.currentThread().interrupt();
                return null;
            }
        }

        /* 缓存中有并且没有过期，直接返回 */
        Optional<ServerParameters> cachedData = serverParametersRepository.findById(key);
        return cachedData.map(serverParameters -> serverParameters.asViewObject(RespServerParametersVO.class)).orElse(null);
    }

    /* TODO: Written by - Han Yongding 2024/06/02 查询服务器信息 */
    @Override
    public RespServiceInformationVO getServiceInformation() {
        String key = Const.REDIS_DATA_KEY;

        /* 缓存中没有或者过期了 */
        if (!Boolean.TRUE.equals(serviceInformationRepository.existsById(key)) || TimeUtils.isExpired(Objects.requireNonNull(serviceInformationRepository.findById(key).get().getExpirationTime()))) {
            RLock lock = redissonClient.getLock(key);
            try {
                /* 尝试获取锁，等待时间10秒，租约时间90秒, 因为获取系统参数方法等待一秒钟获取cpu的使用情况 */

                boolean lockAcquired = lock.tryLock(10, 90, TimeUnit.SECONDS);
                /* TODO: Written by - Han Yongding 2024/06/02 拿到锁 */
                if (lockAcquired) {
                    try {
                        /* 重建缓存 */
                        ServiceInformation serviceInformation = systemUtils.getServiceInformation(key);
                        serviceInformationRepository.save(serviceInformation) ;

                        /* TODO: Written by - Han Yongding 2024/06/02 响应 */
                        return serviceInformation.asViewObject(RespServiceInformationVO.class) ;
                    } finally {
                      /* TODO: Written by - Han Yongding 2024/06/02 释放锁 */
                      lock.unlock();
                    }

                } else {
                    /* 没拿到锁 */

                    /* 防止redis中没有缓存，直接返回会报错，如果没有缓存，要等待缓存重建完成 */
                    while (!Boolean.TRUE.equals(serviceInformationRepository.existsById(key))) {
                        try {
                            /* TODO: Written by - Han Yongding 2024/05/28 等待一小段时间后重新检查缓存是否存在 */
                            TimeUnit.MILLISECONDS.sleep(500);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            LogUtils.error(this.getClass(), "服务信息-线程等待缓存重建时被中断: " + e);
                            return null;
                        }
                    }

                    /* TODO: Written by - Han Yongding 2024/06/02 返回缓存中的数据 */
                    return serviceInformationRepository.findById(key).map(serverParameters -> serverParameters.asViewObject(RespServiceInformationVO.class)).orElse(null);
                }
            } catch (InterruptedException e) {
                LogUtils.error(this.getClass(), "系统参数缓存重建，获取锁异常: " + e);
                /* TODO: Written by - Han Yongding 2024/06/02 恢复中断状态 */
                Thread.currentThread().interrupt();
                return null;
            }

        }
        return serviceInformationRepository.findById(key).map(s -> s.asViewObject(RespServiceInformationVO.class)).orElse(null) ;
    }
}
