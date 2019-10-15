package com.grasswort.picker.user.validator;

import com.grasswort.picker.user.validator.PasswordConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xuliangliang
 * @Classname Password
 * @Description 密码校验
 * @Date 2019/10/15 15:53
 * @blame Java Team
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordConstraintValidator.class)
public @interface Password {

    String message() default "密码为8-20位数字或字母，区分大小写。";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
