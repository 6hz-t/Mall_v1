package cn.net.susan.interceptor;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONUtil;
import cn.net.susan.annotation.ExcelExport;
import cn.net.susan.entity.common.CommonTaskEntity;
import cn.net.susan.enums.ExcelBizTypeEnum;
import cn.net.susan.enums.TaskStatusEnum;
import cn.net.susan.enums.TaskTypeEnum;
import cn.net.susan.mapper.common.CommonTaskMapper;
import cn.net.susan.util.FillUserUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Aspect
@Component
public class CommonTaskAspect {

    @Autowired
    private CommonTaskMapper commonTaskMapper;

    @Pointcut("@annotation(cn.net.susan.annotation.ExcelExport)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) throws Throwable {
        String targetName = joinPoint.getTarget().getClass().getName();
        Class<?> targetClass = Class.forName(targetName);
        // 获取切入方法名
        String methodName = joinPoint.getSignature().getName();
        // 获取切入方法参数
        Object[] arguments = joinPoint.getArgs();
        // 获取目标类的所有方法
        Method[] methods = targetClass.getMethods();
        for (Method method : methods) {
            // 方法名相同、包含目标注解、方法参数个数相同（避免有重载）
            if (method.getName().equals(methodName) && method.isAnnotationPresent(ExcelExport.class)
                    && method.getParameterTypes().length == arguments.length) {
                ExcelBizTypeEnum excelBizTypeEnum = method.getAnnotation(ExcelExport.class).value();

                CommonTaskEntity commonTaskEntity = createCommonTaskEntity(excelBizTypeEnum);
                if (ArrayUtil.isNotEmpty(arguments)) {
                    Object requestParam = arguments[0];
                    commonTaskEntity.setRequestParam(JSONUtil.toJsonStr(requestParam));
                }

                commonTaskMapper.insert(commonTaskEntity);
            }
        }

    }


    private CommonTaskEntity createCommonTaskEntity(ExcelBizTypeEnum excelBizTypeEnum) {
        CommonTaskEntity commonTaskEntity = new CommonTaskEntity();
        commonTaskEntity.setName(getTaskName(excelBizTypeEnum));
        commonTaskEntity.setStatus(TaskStatusEnum.WAITING.getValue());
        commonTaskEntity.setFailureCount(0);
        commonTaskEntity.setType(TaskTypeEnum.EXPORT_EXCEL.getValue());
        commonTaskEntity.setBizType(excelBizTypeEnum.getValue());
        FillUserUtil.fillCreateUserInfo(commonTaskEntity);
        return commonTaskEntity;
    }

    private String getTaskName(ExcelBizTypeEnum excelBizTypeEnum) {
        return String.format("导出%s数据", excelBizTypeEnum.getDesc());
    }
}
