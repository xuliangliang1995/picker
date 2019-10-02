package com.grasswort.picker.user.interceptor;

import com.alibaba.fastjson.JSON;
import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.user.IUserLoginService;
import com.grasswort.picker.user.annotation.Anoymous;
import com.grasswort.picker.user.constants.JwtTokenConstants;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dto.CheckAuthRequest;
import com.grasswort.picker.user.dto.CheckAuthResponse;
import com.grasswort.picker.user.exception.TokenExpiredException;
import com.grasswort.picker.user.exception.TokenVerifyFailException;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author xuliangliang
 * @Classname TokenInterceptor
 * @Description token 校验
 * @Date 2019/10/2 11:38
 * @blame Java Team
 */
public class TokenInterceptor extends HandlerInterceptorAdapter {
    @Reference(version = "1.0", timeout = 1000)
    IUserLoginService iUserLoginService;

    private final String CONTENT_TYPE_TEXT_HTML_UTF8 = "text/html;charset=UTF-8";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (! (handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod)handler;
        if (isAnoymous(handlerMethod)) {
            return true;
        }

        String token = request.getHeader(JwtTokenConstants.JWT_TOKEN_KEY);
        if (StringUtils.isBlank(token)) {
            /*ResponseData responseData = new ResponseUtil().setErrorMsg("token 已失效！");
            response.setContentType(CONTENT_TYPE_TEXT_HTML_UTF8);
            response.getWriter().write(JSON.toJSON(responseData).toString());
            return false;*/
            throw TokenExpiredException.getInstance();
        }

        CheckAuthRequest authRequest = new CheckAuthRequest();
        authRequest.setToken(token);
        CheckAuthResponse authResponse = iUserLoginService.validToken(authRequest);
        if (SysRetCodeConstants.SUCCESS.getCode().equals(authResponse.getCode())) {
            // 暂不做任何操作
            return super.preHandle(request, response, handler);
        }
        /*ResponseData responseData = new ResponseUtil().setErrorMsg(authResponse.getMsg());
        response.setContentType(CONTENT_TYPE_TEXT_HTML_UTF8);
        response.getWriter().write(JSON.toJSON(responseData).toString());*/
       throw TokenVerifyFailException.getInstance();
    }

    private boolean isAnoymous(HandlerMethod handlerMethod) {
        Object bean = handlerMethod.getBean();
        Class clazz=bean.getClass();
        if (clazz.getAnnotation(Anoymous.class) != null) {
            return true;
        }
        Method method = handlerMethod.getMethod();
        return method.getAnnotation(Anoymous.class) != null;
    }
}
