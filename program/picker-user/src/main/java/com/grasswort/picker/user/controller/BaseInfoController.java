package com.grasswort.picker.user.controller;

import com.grasswort.picker.commons.constants.TOrF;
import com.grasswort.picker.commons.ip.PickerIpUtil;
import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.commons.validator.ValidatorTool;
import com.grasswort.picker.user.IUserBaseInfoService;
import com.grasswort.picker.user.constants.JwtTokenConstants;
import com.grasswort.picker.user.dto.*;
import com.grasswort.picker.user.model.PickerInfoHolder;
import com.grasswort.picker.user.vo.ChangePasswordForm;
import com.grasswort.picker.user.vo.ChangePhoneForm;
import com.grasswort.picker.user.vo.EditBaseInfoForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author xuliangliang
 * @Classname UserBaseInfoController
 * @Description 基本信息
 * @Date 2019/10/9 13:29
 * @blame Java Team
 */
@Api(tags = "Picker 基本信息")
@RestController
@RequestMapping("/info")
public class BaseInfoController {

    @Reference(version = "1.0", timeout = 10000, validation = TOrF.FALSE, mock = TOrF.FALSE)
    IUserBaseInfoService iUserBaseInfoService;

    @ApiOperation(value = "获取用户基本信息")
    @GetMapping
    public ResponseData<UserBaseInfoResponse> userInfo() {
        UserBaseInfoRequest baseInfoRequest = UserBaseInfoRequest.Builder.anUserBaseInfoRequest()
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .build();

        UserBaseInfoResponse baseInfoResponse = iUserBaseInfoService.userBaseInfo(baseInfoRequest);

        return Optional.ofNullable(baseInfoResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<UserBaseInfoResponse>().setData(baseInfoResponse)
                        : new ResponseUtil<UserBaseInfoResponse>().setErrorMsg(baseInfoResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "编辑用户基本信息")
    @PutMapping
    public ResponseData<UserBaseInfoEditResponse> editUserInfo(@RequestBody @Validated EditBaseInfoForm editBaseInfoForm,
                                     BindingResult bindingResult) {
        ValidatorTool.check(bindingResult);

        UserBaseInfoEditRequest editRequest = UserBaseInfoEditRequest.Builder.anUserBaseInfoEditRequest()
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .withName(editBaseInfoForm.getName())
                .withSex(editBaseInfoForm.getSex())
                .withAvatar(editBaseInfoForm.getAvatar())
                .withSignature(editBaseInfoForm.getSignature())
                .build();

        UserBaseInfoEditResponse editResponse = iUserBaseInfoService.editUserBaseInfo(editRequest);

        return Optional.ofNullable(editResponse)
                .map(r -> r.isSuccess()
                            ? new ResponseUtil<UserBaseInfoEditResponse>().setData(null)
                            : new ResponseUtil<UserBaseInfoEditResponse>().setErrorMsg(editResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "修改用户密码（需要提高权限）")
    @PatchMapping("/password")
    public ResponseData changePassword(
            @RequestBody @Validated ChangePasswordForm changePasswordForm, BindingResult bindingResult,
            HttpServletRequest request, HttpServletResponse response
    ) {
        ValidatorTool.check(bindingResult);

        UserChangePwdRequest changePwdRequest = UserChangePwdRequest.Builder.anUserChangePwdRequest()
                .withAccessToken(request.getHeader(JwtTokenConstants.JWT_ACCESS_TOKEN_KEY))
                .withPassword(changePasswordForm.getPassword())
                .withIp(PickerIpUtil.getIp(request))
                .build();

        UserChangePwdResponse changePwdResponse = iUserBaseInfoService.changePwd(changePwdRequest);

        return Optional.ofNullable(changePwdResponse)
                .map(r -> {
                    if (r.isSuccess()) {
                        response.setHeader(JwtTokenConstants.JWT_ACCESS_TOKEN_KEY, changePwdResponse.getAccessToken());
                        response.setHeader(JwtTokenConstants.JWT_REFRESH_TOKEN_KEY, changePwdResponse.getRefreshToken());
                        return new ResponseUtil<>().setData(null, "修改成功");
                    }
                    return new ResponseUtil<>().setErrorMsg(changePwdResponse.getMsg());
                })
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "修改手机号（需要提高权限）")
    @PatchMapping("/phone")
    public ResponseData changPhone(
            @RequestBody @Validated ChangePhoneForm form, BindingResult bindingResult,
            HttpServletRequest request) {
        ValidatorTool.check(bindingResult);

        UserChangePhoneRequest changePhoneRequest = UserChangePhoneRequest.Builder.anUserChangePhoneRequest()
                .withAccessToken(request.getHeader(JwtTokenConstants.JWT_ACCESS_TOKEN_KEY))
                .withPhone(form.getPhone())
                .withCaptcha(form.getCaptcha())
                .withIp(PickerIpUtil.getIp(request))
                .build();

        UserChangePhoneResponse changePhoneResponse = iUserBaseInfoService.changePhone(changePhoneRequest);

        return Optional.ofNullable(changePhoneResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(null, "修改成功")
                        : new ResponseUtil<>().setErrorMsg(changePhoneResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }
 }
