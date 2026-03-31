package cn.net.susan.interceptor;

import cn.net.susan.annotation.Limit;
import cn.net.susan.exception.BusinessException;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Map;


@Slf4j
@Aspect
public class SimpleLimitAspect {

    private static final String LIMIT_ERROR_MESSAGE = "系统繁忙,请稍后再试";

    private final Map<String, RateLimiter> limitMap = Maps.newConcurrentMap();

    @Around("@annotation(cn.net.susan.annotation.Limit)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //拿limit的注解
        Limit limit = method.getAnnotation(Limit.class);
        if (limit != null) {
            String key = limit.key();
            RateLimiter rateLimiter = null;
            //验证缓存是否有命中key
            if (!limitMap.containsKey(key)) {
                // 创建令牌桶
                rateLimiter = RateLimiter.create(limit.permitsPerSecond());
                limitMap.put(key, rateLimiter);
                log.info("新建了令牌桶={}，容量={}", key, limit.permitsPerSecond());
            }
            rateLimiter = limitMap.get(key);
            // 拿令牌
            boolean acquire = rateLimiter.tryAcquire(limit.timeOut(), limit.timeUnit());
            // 拿不到命令，直接返回异常提示
            if (!acquire) {
                throw new BusinessException(LIMIT_ERROR_MESSAGE);
            }
        }
        return joinPoint.proceed();
    }
}
