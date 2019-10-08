package com.grasswort.picker.user.advice;

import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.user.exception.AbstractTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * @author xuliangliang
 * @Classname ExceptionHandler
 * @Description 异常处理
 * @Date 2019/9/24 19:41
 * @blame Java Team
 */
@Slf4j
@ControllerAdvice
public class ExceptionAdvice {

    /**
     * 参数校验失败异常
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseData validatorException(
            HttpServletRequest request,
            ConstraintViolationException exception
    ) {
        exception.printStackTrace();
        String message = exception.getConstraintViolations().stream().findFirst().map(ConstraintViolation::getMessage).get();
        log.info("\n【参数校验失败】：{}", message);
        return new ResponseUtil<>().setErrorMsg(message);
    }

    /**
     * token 校验异常
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(AbstractTokenException.class)
    @ResponseBody
    public ResponseData tokenException(
            HttpServletRequest request,
            AbstractTokenException exception
    ) {
        exception.printStackTrace();
        String message = exception.getMessage();
        log.info("\n【token异常】:{}", message);
        return new ResponseUtil<>().setErrorMsg(message);
    }
}
