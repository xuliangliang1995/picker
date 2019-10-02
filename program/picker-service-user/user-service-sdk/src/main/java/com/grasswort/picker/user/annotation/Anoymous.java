package com.grasswort.picker.user.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xuliangliang
 * @Classname Anoymous
 * @Description 允许匿名请求
 * @Date 2019/10/2 11:38
 * @blame Java Team
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Anoymous {}
