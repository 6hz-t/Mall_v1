package cn.net.susan.interceptor;

import cn.hutool.core.util.ArrayUtil;
import cn.net.susan.annotation.VerifySign;
import cn.net.susan.entity.SignEntity;
import cn.net.susan.exception.BusinessException;
import cn.net.susan.helper.IpWhiteListHelper;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;


@Slf4j
@Aspect
@Component
public class VerifySignAspect {

    @Autowired
    private IpWhiteListHelper ipWhiteListHelper;

    @Value("${mall.mgt.httpRequest.secretKey:susan123}")
    private String secretKey;

    @Pointcut("@annotation(cn.net.susan.annotation.VerifySign)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.isAnnotationPresent(VerifySign.class)) {
            if (!ipWhiteListHelper.checkIp()) {
                throw new BusinessException("非法请求");
            }
            Object[] argValues = joinPoint.getArgs();
            String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
            AssertUtil.notNull(ArrayUtil.isNotEmpty(argNames), "请求参数不能为空");
            AssertUtil.notNull(ArrayUtil.isNotEmpty(argValues), "请求参数值不能为空");

            Object argValue = argValues[0];
            if (!(argValue instanceof SignEntity)) {
                throw new BusinessException("请求参数错误");
            }

            SignEntity signEntity = (SignEntity) argValue;
            AssertUtil.notNull(signEntity.getTimestamp(), "timestamp不能为空");
            AssertUtil.isTrue(StringUtils.hasLength(signEntity.getSign()), "sign不能为空");

            SignUtil.checkSign(signEntity, secretKey);
        }
    }

}
