package com.grasswort.picker.user.controller;

import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.user.IUserTokenRefreshService;
import com.grasswort.picker.user.annotation.Anoymous;
import com.grasswort.picker.user.constants.JwtTokenConstants;
import com.grasswort.picker.user.dto.RefreshAccessTokenRequest;
import com.grasswort.picker.user.dto.RefreshAccessTokenResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author xuliangliang
 * @Classname TokenController
 * @Description token
 * @Date 2019/10/8 22:14
 * @blame Java Team
 */
@Api(tags = "Token 刷新")
@Anoymous
@RestController
@RequestMapping("/token")
public class TokenController {
    @Reference(version = "1.0", timeout = 10000)
    IUserTokenRefreshService iUserTokenRefreshService;

    /**
     * 刷新 token
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "access_token", notes = "通过 refresh_token 获取新的 access_token")
    @GetMapping
    public ResponseData refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader(JwtTokenConstants.JWT_REFRESH_TOKEN_KEY);

        RefreshAccessTokenRequest refreshRequest = RefreshAccessTokenRequest.Builder.aRefreshAccessTokenRequest()
                .withRefreshToken(refreshToken)
                .build();

        RefreshAccessTokenResponse refreshResponse = iUserTokenRefreshService.refreshAccessToken(refreshRequest);

        return Optional.ofNullable(refreshResponse)
                .map(r -> {
                    if (r.isSuccess()) {
                        response.setHeader(JwtTokenConstants.JWT_ACCESS_TOKEN_KEY, refreshResponse.getAccessToken());
                        response.setHeader(JwtTokenConstants.JWT_REFRESH_TOKEN_KEY, refreshResponse.getRefreshToken());
                        return new ResponseUtil<>().setData(null);
                    }
                    return new ResponseUtil<>().setErrorMsg(refreshResponse.getMsg());
                })
                .orElse(ResponseData.SYSTEM_ERROR);

    }
}
