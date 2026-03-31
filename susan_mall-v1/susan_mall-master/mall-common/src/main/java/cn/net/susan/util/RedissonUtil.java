package cn.net.susan.util;

import cn.net.susan.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;


@Component
@Slf4j
public class RedissonUtil {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 尝试锁定某个资源
     *
     * @param key       资源
     * @param waitTime  加锁等待时间
     * @param leaseTime 锁持有时间
     * @param supplier  业务操作
     * @param <T>       返回值泛型
     * @return 返回值
     */
    public <T> T tryLock(String key, long waitTime, long leaseTime, Supplier<T> supplier) {
        AssertUtil.isTrue(StringUtils.hasLength(key), "key不能为空");
        RLock rLock = redissonClient.getLock(key);
        return doTryLock(rLock, key, waitTime, leaseTime, supplier);
    }

    /**
     * 尝试锁定多个资源
     *
     * @param keys      资源集合
     * @param waitTime  加锁等待时间
     * @param leaseTime 锁持有时间
     * @param supplier  业务操作
     * @param <T>       返回值泛型
     * @return 返回值
     */
    public <T> T tryMultiLock(List<String> keys, long waitTime, long leaseTime, Supplier<T> supplier) {
        AssertUtil.notEmpty(keys, "keys不能为空");

        RLock[] rLocks = new RLock[keys.size()];
        for (int i = 0; i < keys.size(); i++) {
            rLocks[i] = redissonClient.getLock(keys.get(i));
        }
        RedissonMultiLock redissonMultiLock = new RedissonMultiLock(rLocks);
        String collectKey = keys.stream().collect(Collectors.joining());
        return doTryLock(redissonMultiLock, collectKey, waitTime, leaseTime, supplier);
    }


    private <T> T doTryLock(RLock rLock, String key, long waitTime, long leaseTime, Supplier<T> supplier) {
        try {
            boolean success = rLock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
            if (success) {
                try {
                    return supplier.get();
                } finally {
                    rLock.unlock();
                }
            } else {
                log.info("分布式锁加锁失败, key:{}", key);
                throw new BusinessException("服务器内部错误");
            }
        } catch (InterruptedException e) {
            log.info("获取分布式锁请求被中断, key:{}", key);
            throw new BusinessException("服务器内部错误");
        }
    }
}
