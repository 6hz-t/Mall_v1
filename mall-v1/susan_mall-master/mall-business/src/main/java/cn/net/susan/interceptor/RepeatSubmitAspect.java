package cn.net.susan.interceptor;

import cn.net.susan.annotation.RepeatSubmit;
import cn.net.susan.exception.BusinessException;
import cn.net.susan.util.IpUtil;
import cn.net.susan.util.Md5Util;
import cn.net.susan.util.RedisUtil;
import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;


@Aspect
@Component
public class RepeatSubmitAspect {

    private static final String REPEAT_SUBMIT_DEFAULT_KEY = "1";
    private static final String REPEAT_SUBMIT_PREFIX = "repeatSubmit:";

    @Autowired
    private RedisUtil redisUtil;

    @Pointcut("@annotation(cn.net.susan.annotation.RepeatSubmit)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) throws Throwable {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        String requestURI = httpServletRequest.getRequestURI();
        String clientIp = IpUtil.getIpAddr(httpServletRequest);
        Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
        String params = JSON.toJSONString(parameterMap);
        String key = getKey(requestURI, clientIp, params);
        String value = redisUtil.get(key);
        if (StringUtils.hasLength(value)) {
            throw new BusinessException("该用户请求已存在，请勿重复提交");
        }

        String targetName = joinPoint.getTarget().getClass().getName();
        Class<?> targetClass = Class.forName(targetName);
        // 获取切入方法名
        String methodName = joinPoint.getSignature().getName();
        Method[] methods = targetClass.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName) && method.isAnnotationPresent(RepeatSubmit.class)) {
                int second = method.getAnnotation(RepeatSubmit.class).second();
                redisUtil.set(key, REPEAT_SUBMIT_DEFAULT_KEY, second);
                return;
            }
        }

    }


    private String getKey(String requestURI, String clientIp, String params) {
        String value = String.format("%s%s%s", requestURI, clientIp, params);
        return String.format("%s%s", REPEAT_SUBMIT_PREFIX, Md5Util.md5(value));
    }
}
