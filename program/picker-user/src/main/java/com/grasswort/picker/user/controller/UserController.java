package com.grasswort.picker.user.controller;

import com.grasswort.picker.commons.constants.TOrF;
import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.commons.validator.ValidatorTool;
import com.grasswort.picker.user.IUserBaseInfoService;
import com.grasswort.picker.user.IUserLoginService;
import com.grasswort.picker.user.IUserRegisterService;
import com.grasswort.picker.user.annotation.Anoymous;
import com.grasswort.picker.user.constants.JwtTokenConstants;
import com.grasswort.picker.user.dto.*;
import com.grasswort.picker.user.util.PickerIdEncrypt;
import com.grasswort.picker.user.vo.LoginForm;
import com.grasswort.picker.user.vo.SignUpForm;
import com.grasswort.picker.user.vo.SignUpVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

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

    @Reference(version = "1.0", timeout = 10000)
    IUserBaseInfoService iUserBaseInfoService;

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

        return Optional.ofNullable(result)
                .map(r -> {
                    if (r.isSuccess()) {
                        SignUpVO signUpVO = new SignUpVO();
                        signUpVO.setEmail(result.getEmail());
                        return new ResponseUtil<SignUpVO>().setData(signUpVO);
                    }
                    return new ResponseUtil<SignUpVO>().setErrorMsg(result.getMsg());
                })
                .orElse(ResponseData.SYSTEM_ERROR);
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

        return Optional.ofNullable(loginResponse)
                .map(r -> {
                    if (r.isSuccess()) {
                        response.setHeader(JwtTokenConstants.JWT_ACCESS_TOKEN_KEY, loginResponse.getAccessToken());
                        response.setHeader(JwtTokenConstants.JWT_REFRESH_TOKEN_KEY, loginResponse.getRefreshToken());
                        return new ResponseUtil<>().setData(null, "登录成功");
                    }
                    return new ResponseUtil<>().setErrorMsg(loginResponse.getMsg());
                })
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "获取作者信息")
    @GetMapping("/{pickerID}")
    public ResponseData authorInfo(@PathVariable("pickerID")String pickerID) {
        Long pickerId = PickerIdEncrypt.decrypt(pickerID);
        if (pickerId == null) {
            return new ResponseUtil<>().setErrorMsg("访问用户不存在！");
        }
        UserBaseInfoRequest baseInfoRequest = UserBaseInfoRequest.Builder.anUserBaseInfoRequest()
                .withUserId(pickerId)
                .build();
        UserBaseInfoResponse baseInfoResponse = iUserBaseInfoService.userBaseInfo(baseInfoRequest);
        return Optional.ofNullable(baseInfoResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(baseInfoResponse)
                        : new ResponseUtil<>().setErrorMsg(baseInfoResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }
}
