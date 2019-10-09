package com.grasswort.picker.user.controller;

import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.commons.validator.ValidatorTool;
import com.grasswort.picker.user.IUserBaseInfoService;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dto.UserBaseInfoEditRequest;
import com.grasswort.picker.user.dto.UserBaseInfoEditResponse;
import com.grasswort.picker.user.dto.UserBaseInfoRequest;
import com.grasswort.picker.user.dto.UserBaseInfoResponse;
import com.grasswort.picker.user.model.PickerInfoHolder;
import com.grasswort.picker.user.vo.EditBaseInfoForm;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author xuliangliang
 * @Classname UserBaseInfoController
 * @Description 基本信息
 * @Date 2019/10/9 13:29
 * @blame Java Team
 */
@RestController
@RequestMapping("/user/info")
public class UserBaseInfoController {

    @Reference(version = "1.0")
    IUserBaseInfoService iUserBaseInfoService;

    @GetMapping
    public ResponseData<UserBaseInfoResponse> userInfo() {
        UserBaseInfoRequest baseInfoRequest = new UserBaseInfoRequest();
        baseInfoRequest.setUserId(PickerInfoHolder.getPickerInfo().getId());
        UserBaseInfoResponse baseInfoResponse = iUserBaseInfoService.userBaseInfo(baseInfoRequest);
        if (SysRetCodeConstants.SUCCESS.getCode().equals(baseInfoResponse.getCode())) {
            return new ResponseUtil<UserBaseInfoResponse>().setData(baseInfoResponse);
        }
        return new ResponseUtil<UserBaseInfoResponse>().setErrorMsg(baseInfoResponse.getMsg());
    }

    @PutMapping
    public ResponseData<UserBaseInfoEditResponse> editUserInfo(@RequestBody @Validated EditBaseInfoForm editBaseInfoForm,
                                     BindingResult bindingResult) {
        ValidatorTool.check(bindingResult);
        UserBaseInfoEditRequest editRequest = new UserBaseInfoEditRequest();
        editRequest.setUserId(PickerInfoHolder.getPickerInfo().getId());
        editRequest.setName(editBaseInfoForm.getName());
        editRequest.setSex(editBaseInfoForm.getSex());
        editRequest.setPhone(editBaseInfoForm.getPhone());
        editRequest.setEmail(editBaseInfoForm.getEmail());
        UserBaseInfoEditResponse editResponse = iUserBaseInfoService.editUserBaseInfo(editRequest);
        if (SysRetCodeConstants.SUCCESS.getCode().equals(editResponse.getCode())) {
            return new ResponseUtil<UserBaseInfoEditResponse>().setData(editResponse);
        }
        return new ResponseUtil<UserBaseInfoEditResponse>().setErrorMsg(editResponse.getMsg());
    }
 }
