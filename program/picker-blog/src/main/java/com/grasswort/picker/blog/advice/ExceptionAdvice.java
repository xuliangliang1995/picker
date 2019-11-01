package com.grasswort.picker.blog.advice;

import com.grasswort.picker.commons.exception.InvalidParamException;
import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.user.exception.AbstractTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * @author xuliangliang
 * @Classname ExceptionAdvice
 * @Description 全局异常处理
 * @Date 2019/10/21 14:40
 * @blame Java Team
 */
@Slf4j
@ControllerAdvice
public class ExceptionAdvice {
    /**
     * 参数校验失败异常
     * @param exception
     * @return
     */
    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseBody
    public ResponseData validatorException1(
            ConstraintViolationException exception
    ) {
        String message = exception.getConstraintViolations().stream().findFirst().map(cv -> cv.getMessage() + ":" + cv.getInvalidValue()).get();
        log.info("\n【参数校验失败】：{}", message);
        return new ResponseUtil<>().setErrorMsg(message);
    }

    /**
     * 参数校验
     * @param exception
     * @return
     */
    @ExceptionHandler({InvalidParamException.class})
    @ResponseBody
    public ResponseData validatorException2(
            InvalidParamException exception
    ) {
        log.info("\n【参数校验失败】：{}", exception.getMessage());
        return new ResponseUtil<>().setErrorMsg(exception.getMessage());
    }

    /**
     * token 校验异常
     * @param exception
     * @return
     */
    @ExceptionHandler(AbstractTokenException.class)
    @ResponseBody
    public ResponseData tokenException(
            AbstractTokenException exception
    ) {
        String message = exception.getMessage();
        log.info("\n【token异常】:{}", message);
        return new ResponseUtil<>().setErrorMsg(message);
    }
}
