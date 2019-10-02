package com.grasswort.picker.commons.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xuliangliang
 * @Classname DB
 * @Description 数据库选择注解
 * @Date 2019/10/2 16:38
 * @blame Java Team
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DB {
    String value() default "";
}
