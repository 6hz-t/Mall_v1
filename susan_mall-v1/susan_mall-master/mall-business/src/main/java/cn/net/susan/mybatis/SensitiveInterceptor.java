package cn.net.susan.mybatis;

import cn.net.susan.annotation.Sensitive;
import cn.net.susan.enums.SensitiveTypeEnum;
import cn.net.susan.sensitive.ICustomMaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;

import java.beans.Statement;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;


@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class SensitiveInterceptor implements Interceptor {
    @Autowired
    private ICustomMaskService customMaskService;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = invocation.proceed();
        log.debug("进入数据脱敏拦截器...");
        if (result instanceof List) {
            List<?> resultList = (List<?>) result;
            for (Object obj : resultList) {
                doSensitiveFields(obj);
            }
        } else if (result instanceof Map) {
            Map<?, ?> resultMap = (Map<?, ?>) result;
            for (Object obj : resultMap.values()) {
                doSensitiveFields(obj);
            }
        } else {
            doSensitiveFields(result);
        }
        return result;
    }

    private void doSensitiveFields(Object obj) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Sensitive.class)) {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value == null) {
                    return;
                }
                Sensitive sensitiveData = field.getAnnotation(Sensitive.class);
                SensitiveTypeEnum type = sensitiveData.type();
                String result;
                if (type == SensitiveTypeEnum.CUSTOM) {
                    result = type.maskSensitiveData(value.toString(), customMaskService);
                } else {
                    result = type.maskSensitiveData(value.toString());
                }
                field.set(obj, result);
            }
        }
    }
}
