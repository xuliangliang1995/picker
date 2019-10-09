package com.grasswort.picker.commons.validator;

import com.grasswort.picker.commons.exception.InvalidParamException;
import org.springframework.validation.BindingResult;

/**
 * @author xuliangliang
 * @Classname ValidatorTool
 * @Description HibernateValidator 校验工具
 * @Date 2019/10/9 13:53
 * @blame Java Team
 */
public class ValidatorTool {

    public static void check(BindingResult result) {
        if (result.hasErrors() & result.getFieldErrors().size() > 0) {
            throw new InvalidParamException(result.getFieldErrors().get(0).getDefaultMessage());
        }
    }
}
