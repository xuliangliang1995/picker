package com.grasswort.picker.user.interceptor;

import com.grasswort.picker.commons.ip.PickerIpUtil;
import com.grasswort.picker.user.IUserLoginService;
import com.grasswort.picker.user.annotation.Anoymous;
import com.grasswort.picker.user.constants.JwtTokenConstants;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dto.CheckAuthRequest;
import com.grasswort.picker.user.dto.CheckAuthResponse;
import com.grasswort.picker.user.exception.TokenExpiredException;
import com.grasswort.picker.user.exception.TokenVerifyFailException;
import com.grasswort.picker.user.model.PickerInfoHolder;
import com.grasswort.picker.user.model.PickerInfo;
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
    @Reference(version = "1.0", timeout = 10000)
    IUserLoginService iUserLoginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (! (handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod)handler;
        if (isAnoymous(handlerMethod)) {
            return true;
        }

        String token = request.getHeader(JwtTokenConstants.JWT_ACCESS_TOKEN_KEY);
        if (StringUtils.isBlank(token)) {
            throw TokenExpiredException.getInstance();
        }

        CheckAuthRequest authRequest = new CheckAuthRequest();
        authRequest.setToken(token);
        authRequest.setIp(PickerIpUtil.getIp(request));
        CheckAuthResponse authResponse = iUserLoginService.validToken(authRequest);
        if (SysRetCodeConstants.SUCCESS.getCode().equals(authResponse.getCode())) {
            PickerInfoHolder.setPickerInfo(PickerInfo.builder()
                    .id(authResponse.getId())
                    .name(authResponse.getName())
                    .privilege(authResponse.isPrivilege())
                    .build());
            return super.preHandle(request, response, handler);
        }
       throw TokenVerifyFailException.getInstance();
    }

    /**
     * 是否允许匿名请求
     * @param handlerMethod
     * @return
     */
    private boolean isAnoymous(HandlerMethod handlerMethod) {
        Object bean = handlerMethod.getBean();
        Class clazz = bean.getClass();
        if (clazz.getAnnotation(Anoymous.class) != null) {
            return true;
        }
        Method method = handlerMethod.getMethod();
        return method.getAnnotation(Anoymous.class) != null;
    }
}
