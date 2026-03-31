package cn.net.susan.interceptor;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.net.susan.annotation.BizLog;
import cn.net.susan.entity.log.BizLogEntity;
import cn.net.susan.service.log.BizLogService;
import cn.net.susan.util.IpUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;


@Aspect
@Component
public class BizLogAspect {

    @Autowired
    private BizLogService bizLogService;

    @Pointcut("@annotation(cn.net.susan.annotation.BizLog)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        Object result = joinPoint.proceed();
        long time = System.currentTimeMillis() - startTime;

        BizLogEntity bizLogEntity = createBizLogEntity(joinPoint, httpServletRequest);
        bizLogEntity.setTime((int) time);
        bizLogEntity.setStatus(1);
        bizLogService.save(bizLogEntity);
        return result;
    }

    private String getParam(JoinPoint joinPoint) {
        StringBuilder params = new StringBuilder("{");
        Object[] argValues = joinPoint.getArgs();
        String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        if (argValues != null) {
            for (int i = 0; i < argValues.length; i++) {
                params.append(" ").append(argNames[i]).append(": ").append(argValues[i]);
            }
        }
        return params.append("}").toString();
    }

    private BizLogEntity createBizLogEntity(JoinPoint joinPoint, HttpServletRequest httpServletRequest) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        BizLog bizLog = method.getAnnotation(BizLog.class);
        String methodName = joinPoint.getTarget().getClass().getName() + "." + signature.getName();

        BizLogEntity bizLogEntity = new BizLogEntity();
        bizLogEntity.setDescription(bizLog.value());
        bizLogEntity.setMethodName(methodName);
        bizLogEntity.setStatus(1);
        bizLogEntity.setRequestIp(IpUtil.getIpAddr(httpServletRequest));
        bizLogEntity.setUrl(httpServletRequest.getRequestURI());
        bizLogEntity.setBrowser(getBrowserName(httpServletRequest));
        bizLogEntity.setParam(getParam(joinPoint));
        return bizLogEntity;
    }

    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        BizLogEntity bizLogEntity = createBizLogEntity(joinPoint, httpServletRequest);
        bizLogEntity.setStatus(0);
        bizLogEntity.setException(e.getMessage());
        bizLogService.save(bizLogEntity);
    }


    private String getBrowserName(HttpServletRequest httpServletRequest) {
        String userAgentString = httpServletRequest.getHeader("User-Agent");
        UserAgent ua = UserAgentUtil.parse(userAgentString);
        return ua.getBrowser().toString();
    }
}
