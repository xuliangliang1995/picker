package com.grasswort.picker.commons.annotation;

import java.lang.annotation.*;

/**
 * @author xuliangliang
 * @Classname Slave
 * @Description 主从数据库（从）
 * @Date 2019/9/29 18:16
 * @blame Java Team
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Slave {
}
