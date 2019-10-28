package com.grasswort.picker.user.validator;

import com.grasswort.picker.user.constants.RegexConstants;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xuliangliang
 * @Classname UsernameConstraintValidator
 * @Description 用户名校验
 * @Date 2019/10/15 15:50
 * @blame Java Team
 */
public class UsernameConstraintValidator implements ConstraintValidator<Username, String> {

    private static final Pattern pattern = Pattern.compile(RegexConstants.USERNAME_REGEX);

    @Override
    public void initialize(Username constraintAnnotation) {

    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }
}
