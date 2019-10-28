package com.grasswort.picker.user.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xuliangliang
 * @Classname Mobile
 * @Description 手机号校验
 * @Date 2019/10/15 15:56
 * @blame Java Team
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MobileConstraintValidator.class)
public @interface Mobile {

    String message() default "手机号码格式有误。";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
