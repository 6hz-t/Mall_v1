package cn.net.susan.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
    public @interface RepeatSubmit {

    /**
     * 限制时间，单位秒
     *
     * @return 秒
     */
    int second() default 5;
}
