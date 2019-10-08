package com.grasswort.picker.user.controller;

import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.user.IUserTokenRefreshService;
import com.grasswort.picker.user.annotation.Anoymous;
import com.grasswort.picker.user.constants.JwtTokenConstants;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dto.RefreshAccessTokenRequest;
import com.grasswort.picker.user.dto.RefreshAccessTokenResponse;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xuliangliang
 * @Classname TokenController
 * @Description token
 * @Date 2019/10/8 22:14
 * @blame Java Team
 */
@RestController
@RequestMapping("/user/token")
public class TokenController {
    @Reference(version = "1.0", timeout = 2000)
    IUserTokenRefreshService iUserTokenRefreshService;

    /**
     * 刷新 token
     * @param request
     * @param response
     * @return
     */
    @Anoymous
    @GetMapping
    public ResponseData refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader(JwtTokenConstants.JWT_REFRESH_TOKEN_KEY);
        RefreshAccessTokenRequest refreshRequest = new RefreshAccessTokenRequest();
        refreshRequest.setRefreshToken(refreshToken);
        RefreshAccessTokenResponse refreshResponse = iUserTokenRefreshService.refreshAccessToken(refreshRequest);
        if (SysRetCodeConstants.SUCCESS.getCode().equals(refreshResponse.getCode())) {
            response.setHeader(JwtTokenConstants.JWT_ACCESS_TOKEN_KEY, refreshResponse.getAccessToken());
            response.setHeader(JwtTokenConstants.JWT_REFRESH_TOKEN_KEY, refreshResponse.getRefreshToken());
            return new ResponseUtil<>().setData(null);
        }
        return new ResponseUtil<>().setErrorMsg(refreshResponse.getMsg());
    }
}
