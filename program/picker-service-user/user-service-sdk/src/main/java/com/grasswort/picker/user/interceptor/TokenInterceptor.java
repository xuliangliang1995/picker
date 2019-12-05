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
import java.lang.annotation.Annotation;
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
        Anoymous anoymous = getAnoymous(handlerMethod);
        if (anoymous != null && ! anoymous.resolve()) {
            return true;
        }

        // 是否强制身份校验
        boolean tokenForceValid = anoymous == null;

        String token = request.getHeader(JwtTokenConstants.JWT_ACCESS_TOKEN_KEY);
        if (StringUtils.isNotBlank(token)) {
            CheckAuthRequest authRequest = new CheckAuthRequest();
            authRequest.setToken(token);
            authRequest.setIp(PickerIpUtil.getIp(request));
            CheckAuthResponse authResponse = iUserLoginService.validToken(authRequest);
            if (authResponse != null && authResponse.isSuccess()) {
                PickerInfoHolder.setPickerInfo(PickerInfo.builder()
                        .id(authResponse.getId())
                        .name(authResponse.getName())
                        .privilege(authResponse.isPrivilege())
                        .build());
                return super.preHandle(request, response, handler);
            }
        }

        PickerInfoHolder.setPickerInfo(null);
        // 未通过身份校验、但是要求强制校验的、抛出异常
        if (tokenForceValid) {
            throw TokenVerifyFailException.getInstance();
        }

        return super.preHandle(request, response, handler);
    }

    /**
     * 是否允许匿名请求
     * @param handlerMethod
     * @return
     */
    private Anoymous getAnoymous(HandlerMethod handlerMethod) {
        Object bean = handlerMethod.getBean();
        Method method = handlerMethod.getMethod();
        Anoymous anoymous = method.getAnnotation(Anoymous.class);
        if (anoymous != null) {
            return anoymous;
        }
        Class clazz = bean.getClass();
        anoymous = (Anoymous) clazz.getDeclaredAnnotation(Anoymous.class);
        return anoymous;
    }
}
