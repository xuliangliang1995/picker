package com.grasswort.picker.user.controller;

import com.grasswort.picker.commons.constants.TOrF;
import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.user.IUserLoginService;
import com.grasswort.picker.user.annotation.Anoymous;
import com.grasswort.picker.user.constants.JwtTokenConstants;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dto.UserLoginRequest;
import com.grasswort.picker.user.dto.UserLoginResponse;
import com.grasswort.picker.user.model.PickInfoHolder;
import com.grasswort.picker.user.vo.LoginForm;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author xuliangliang
 * @Classname LoginController
 * @Description 登录
 * @Date 2019/10/2 15:08
 * @blame Java Team
 */
@RestController
@RequestMapping("/user")
public class LoginController {

    @Reference(version = "1.0", timeout = 3000, validation = TOrF.TRUE, mock = TOrF.TRUE)
    IUserLoginService iUserLoginService;

    @Anoymous
    @PostMapping("/login")
    public ResponseData login(@RequestBody @Validated LoginForm loginForm, HttpServletResponse response) {
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUsername(loginForm.getUsername());
        loginRequest.setPassword(loginForm.getPassword());

        UserLoginResponse loginResponse = iUserLoginService.login(loginRequest);
        if (SysRetCodeConstants.SUCCESS.getCode().equals(loginResponse.getCode())) {
            response.setHeader(JwtTokenConstants.JWT_ACCESS_TOKEN_KEY, loginResponse.getAccessToken());
            response.setHeader(JwtTokenConstants.JWT_REFRESH_TOKEN_KEY, loginResponse.getRefreshToken());
            return new ResponseUtil<>().setData(null, "登录成功");
        } else {
            return new ResponseUtil<>().setErrorMsg(loginResponse.getMsg());
        }
    }

    @GetMapping
    public ResponseData pickerInfo() {
        return new ResponseUtil<>().setData(PickInfoHolder.getPickerInfo().getName());
    }
}
