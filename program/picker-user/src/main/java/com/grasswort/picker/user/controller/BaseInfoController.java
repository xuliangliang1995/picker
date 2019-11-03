package com.grasswort.picker.user.controller;

import com.grasswort.picker.commons.constants.TOrF;
import com.grasswort.picker.commons.ip.PickerIpUtil;
import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.commons.validator.ValidatorTool;
import com.grasswort.picker.user.IUserBaseInfoService;
import com.grasswort.picker.user.constants.JwtTokenConstants;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dto.*;
import com.grasswort.picker.user.model.PickerInfoHolder;
import com.grasswort.picker.user.vo.ChangePasswordForm;
import com.grasswort.picker.user.vo.EditBaseInfoForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    @Reference(version = "1.0", timeout = 2000, validation = TOrF.FALSE, mock = TOrF.FALSE)
    IUserBaseInfoService iUserBaseInfoService;

    @ApiOperation(value = "获取用户基本信息")
    @GetMapping
    public ResponseData<UserBaseInfoResponse> userInfo() {
        UserBaseInfoRequest baseInfoRequest = UserBaseInfoRequest.Builder.anUserBaseInfoRequest()
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .build();

        UserBaseInfoResponse baseInfoResponse = iUserBaseInfoService.userBaseInfo(baseInfoRequest);
        if (SysRetCodeConstants.SUCCESS.getCode().equals(baseInfoResponse.getCode())) {
            return new ResponseUtil<UserBaseInfoResponse>().setData(baseInfoResponse);
        }

        return new ResponseUtil<UserBaseInfoResponse>().setErrorMsg(baseInfoResponse.getMsg());
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
                .build();

        UserBaseInfoEditResponse editResponse = iUserBaseInfoService.editUserBaseInfo(editRequest);

        if (SysRetCodeConstants.SUCCESS.getCode().equals(editResponse.getCode())) {
            return new ResponseUtil<UserBaseInfoEditResponse>().setData(editResponse);
        }

        return new ResponseUtil<UserBaseInfoEditResponse>().setErrorMsg(editResponse.getMsg());
    }

    @ApiOperation(value = "修改用户密码（需要提高权限）")
    @PatchMapping("/password")
    public ResponseData changePassword(
            @RequestBody ChangePasswordForm changePasswordForm, BindingResult bindingResult,
            HttpServletRequest request, HttpServletResponse response
    ) {
        ValidatorTool.check(bindingResult);

        UserChangePwdRequest changePwdRequest = UserChangePwdRequest.Builder.anUserChangePwdRequest()
                .withAccessToken(request.getHeader(JwtTokenConstants.JWT_ACCESS_TOKEN_KEY))
                .withPassword(changePasswordForm.getPassword())
                .withIp(PickerIpUtil.getIp(request))
                .build();

        UserChangePwdResponse changePwdResponse = iUserBaseInfoService.changePwd(changePwdRequest);

        if (SysRetCodeConstants.SUCCESS.getCode().equals(changePwdResponse.getCode())) {
            response.setHeader(JwtTokenConstants.JWT_ACCESS_TOKEN_KEY, changePwdResponse.getAccessToken());
            response.setHeader(JwtTokenConstants.JWT_REFRESH_TOKEN_KEY, changePwdResponse.getRefreshToken());
            return new ResponseUtil<>().setData(null, "修改成功");
        }
        return new ResponseUtil<>().setErrorMsg(changePwdResponse.getMsg());
    }
 }
