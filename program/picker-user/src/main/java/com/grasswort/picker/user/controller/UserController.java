package com.grasswort.picker.user.controller;

import com.grasswort.picker.commons.constants.TOrF;
import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.commons.validator.ValidatorTool;
import com.grasswort.picker.user.IUserLoginService;
import com.grasswort.picker.user.IUserRegisterService;
import com.grasswort.picker.user.annotation.Anoymous;
import com.grasswort.picker.user.constants.JwtTokenConstants;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dto.UserLoginRequest;
import com.grasswort.picker.user.dto.UserLoginResponse;
import com.grasswort.picker.user.dto.UserRegisterRequest;
import com.grasswort.picker.user.dto.UserRegisterResponse;
import com.grasswort.picker.user.vo.LoginForm;
import com.grasswort.picker.user.vo.SignUpForm;
import com.grasswort.picker.user.vo.SignUpVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author xuliangliang
 * @Classname SignController
 * @Description 注册和登录
 * @Date 2019/10/9 17:39
 * @blame Java Team
 */
@Api(tags = "Picker 注册、登录")
@Anoymous
@RestController
@RequestMapping("/")
public class UserController {

    @Reference(version = "1.0", timeout = 10000, validation = TOrF.FALSE, mock = TOrF.TRUE)
    IUserRegisterService iUserRegisterService;

    @Reference(version = "1.0", timeout = 10000, validation = TOrF.FALSE, mock = TOrF.TRUE)
    IUserLoginService iUserLoginService;

    @ApiOperation(value = "注册")
    @PostMapping("/signUp")
    public ResponseData<SignUpVO> register(@RequestBody @Validated SignUpForm form, BindingResult bindingResult) {
        ValidatorTool.check(bindingResult);
        UserRegisterRequest signUpRequest = UserRegisterRequest.Builder.anUserRegisterRequest()
                .withUsername(form.getUsername())
                .withPassword(form.getPassword())
                .withEmail(form.getEmail())
                .build();
        UserRegisterResponse result = iUserRegisterService.register(signUpRequest);
        if (result.getCode().equals(SysRetCodeConstants.SUCCESS.getCode())) {
            SignUpVO signUpVO = new SignUpVO();
            signUpVO.setEmail(result.getEmail());
            return new ResponseUtil<SignUpVO>().setData(signUpVO);
        }
        return new ResponseUtil<SignUpVO>().setErrorMsg(result.getMsg());
    }

    @ApiOperation(value = "登录")
    @PostMapping("/signIn")
    public ResponseData login(@RequestBody @Validated LoginForm loginForm, BindingResult bindingResult, HttpServletResponse response) {
        ValidatorTool.check(bindingResult);

        UserLoginRequest loginRequest = UserLoginRequest.Builder.anUserLoginRequest()
                .withUsername(loginForm.getUsername())
                .withPassword(loginForm.getPassword())
                .build();

        UserLoginResponse loginResponse = iUserLoginService.login(loginRequest);
        if (SysRetCodeConstants.SUCCESS.getCode().equals(loginResponse.getCode())) {
            response.setHeader(JwtTokenConstants.JWT_ACCESS_TOKEN_KEY, loginResponse.getAccessToken());
            response.setHeader(JwtTokenConstants.JWT_REFRESH_TOKEN_KEY, loginResponse.getRefreshToken());
            return new ResponseUtil<>().setData(null, "登录成功");
        } else {
            return new ResponseUtil<>().setErrorMsg(loginResponse.getMsg());
        }
    }
}
