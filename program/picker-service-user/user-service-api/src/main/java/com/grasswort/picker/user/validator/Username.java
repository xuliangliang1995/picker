package com.grasswort.picker.user.validator;

import com.grasswort.picker.user.validator.UsernameConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xuliangliang
 * @Classname Username
 * @Description 用户名校验
 * @Date 2019/10/15 15:49
 * @blame Java Team
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameConstraintValidator.class)
public @interface Username {
    String message() default "用户名为8-20位数字或字母，区分大小写。";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
