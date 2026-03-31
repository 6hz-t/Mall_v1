package cn.net.susan.interceptor;

import cn.net.susan.annotation.ValidSensitiveWordField;
import cn.net.susan.service.common.CommonSensitiveWordService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Aspect
@Component
public class ValidSensitiveWordAspect {

    @Autowired
    private CommonSensitiveWordService sensitiveWordService;

    @Pointcut("@annotation(cn.net.susan.annotation.ValidSensitiveWord)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        List<String> content = Lists.newArrayList();
        // 获取参数
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            Object value = args[i];
            if (value instanceof String) {
                content.add((String) value);
            } else {
                parseEntity(value, content);
            }
        }

        validSensitiveWord(content);
        return joinPoint.proceed();
    }

    private void validSensitiveWord(List<String> content) {
        if (CollectionUtils.isEmpty(content)) {
            return;
        }

        String collectText = content.stream().collect(Collectors.joining(","));
        sensitiveWordService.checkSensitiveWord(collectText);
    }


    private void parseEntity(Object object, List<String> content) throws IllegalAccessException {
        if (object instanceof List) {
            List<Object> list = (List<Object>) object;
            for (Object obj : list) {
                parseEntity(obj, content);
            }
        } else if (object instanceof Set) {
            Set<Object> set = (Set<Object>) object;
            for (Object obj : set) {
                parseEntity(obj, content);
            }
        } else {
            List<Field> fields = Lists.newArrayList();
            Class aClass = object.getClass();
            while (Objects.nonNull(aClass)) {
                fields.addAll(Arrays.asList(aClass.getDeclaredFields()));
                aClass = aClass.getSuperclass();
            }

            if (CollectionUtils.isEmpty(fields)) {
                return;
            }

            for (Field field : fields) {
                if (field.isAnnotationPresent(ValidSensitiveWordField.class)) {
                    field.setAccessible(true);
                    Object value = field.get(object);
                    if (Objects.isNull(value)) {
                        continue;
                    }

                    if (value instanceof String) {
                        content.add((String) value);
                    } else {
                        parseEntity(value, content);
                    }
                }
            }
        }
    }
}
