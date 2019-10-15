package com.grasswort.picker.user.validator;

import com.grasswort.picker.user.constants.RegexConstants;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xuliangliang
 * @Classname PasswordConstraintValidator
 * @Description 密码校验
 * @Date 2019/10/15 15:54
 * @blame Java Team
 */
public class PasswordConstraintValidator implements ConstraintValidator<Password, String> {
    private static final Pattern pattern = Pattern.compile(RegexConstants.PASSWORD_REGEX);

    @Override
    public void initialize(Password constraintAnnotation) {

    }

    @Override
    public boolean isValid(String pwd, ConstraintValidatorContext constraintValidatorContext) {
        Matcher matcher = pattern.matcher(pwd);
        return matcher.matches();
    }
}
