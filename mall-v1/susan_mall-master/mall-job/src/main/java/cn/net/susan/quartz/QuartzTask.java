package cn.net.susan.quartz;

import cn.hutool.extra.spring.SpringUtil;
import cn.net.susan.util.SpringBeanUtil;
import com.alibaba.excel.util.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.Callable;


public class QuartzTask implements Callable {

    private static final String DEFAULT_METHOD_NAME = "run";
    private Object target;
    private Method method;
    private String params;

    public QuartzTask(String beanName, String methodName, String params)
            throws NoSuchMethodException, SecurityException {
        this.target = SpringBeanUtil.getBean(beanName);
        this.params = params;

        if (StringUtils.isBlank(methodName) || DEFAULT_METHOD_NAME.equals(methodName)) {
            Class<?> superclass = target.getClass().getSuperclass();
            if (StringUtils.isNotBlank(params)) {
                this.method = superclass.getDeclaredMethod(DEFAULT_METHOD_NAME, String.class);
            } else {
                this.method = superclass.getDeclaredMethod(DEFAULT_METHOD_NAME);
            }
        } else {
            if (StringUtils.isNotBlank(params)) {
                this.method = target.getClass().getDeclaredMethod(methodName, String.class);
            } else {
                this.method = target.getClass().getDeclaredMethod(methodName);
            }
        }
    }

    @Override
    public Object call() throws Exception {
        ReflectionUtils.makeAccessible(method);
        if (StringUtils.isNotBlank(params)) {
            method.invoke(target, params);
        } else {
            method.invoke(target);
        }
        return null;
    }
}
