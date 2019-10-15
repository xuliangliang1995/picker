package com.grasswort.picker.user.validator;

import com.grasswort.picker.user.constants.RegexConstants;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xuliangliang
 * @Classname MobileConstraintValidator
 * @Description 手机号校验
 * @Date 2019/10/15 15:56
 * @blame Java Team
 */
public class MobileConstraintValidator implements ConstraintValidator<Mobile, String> {

    private static final Pattern pattern = Pattern.compile(RegexConstants.MOBILE_REGEX);


    @Override
    public void initialize(Mobile constraintAnnotation) {

    }

    @Override
    public boolean isValid(String mobilePhone, ConstraintValidatorContext constraintValidatorContext) {
        Matcher matcher = pattern.matcher(mobilePhone);
        return matcher.matches();
    }
}
