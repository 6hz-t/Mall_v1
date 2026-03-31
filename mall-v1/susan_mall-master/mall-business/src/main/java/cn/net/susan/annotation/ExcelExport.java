package cn.net.susan.annotation;

import cn.net.susan.enums.ExcelBizTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelExport {

    /**
     * 业务类型
     *
     * @return 业务类型
     */
    ExcelBizTypeEnum value();
}
