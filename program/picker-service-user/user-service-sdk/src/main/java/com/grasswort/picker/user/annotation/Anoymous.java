package com.grasswort.picker.user.annotation;

import org.springframework.core.annotation.AliasFor;

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
public @interface Anoymous {
    /**
     * @Anoymous 代表可以匿名访问
     * @Anoymous(resolve=true)代表允许匿名访问，但如果是登录状态，依然解析用户信息（可以返回个性化信息）
     * 是否解析 header,默认不解析，如果 value 和 resolve 有一个为 true, 则解析 request header 中的用户信息（没有则不解析）
     * @return
     */
    @AliasFor("resolve")
    boolean value() default false;
    @AliasFor("value")
    boolean resolve() default false;

}
