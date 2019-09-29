package com.grasswort.picker.commons.annotation;

import java.lang.annotation.*;

/**
 * @author xuliangliang
 * @Classname Master
 * @Description 主从数据库（主）（默认选择的就是主，而且标记了 @Slave 注解的执行完毕后也会清空状态，所以 @Master 注解可以不使用）
 * @Date 2019/9/29 18:15
 * @blame Java Team
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Master {
}
