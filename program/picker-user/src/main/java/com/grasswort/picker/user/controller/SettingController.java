package com.grasswort.picker.user.controller;

import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.commons.validator.ValidatorTool;
import com.grasswort.picker.user.IUserSettingService;
import com.grasswort.picker.user.dto.GetSettingRequest;
import com.grasswort.picker.user.dto.GetSettingResponse;
import com.grasswort.picker.user.dto.SaveSettingRequest;
import com.grasswort.picker.user.dto.SaveSettingResponse;
import com.grasswort.picker.user.model.PickerInfoHolder;
import com.grasswort.picker.user.vo.UserSettingForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author xuliangliang
 * @Classname SettingController
 * @Description 用户配置
 * @Date 2019/11/4 16:12
 * @blame Java Team
 */
@Api(tags = "用户设置")
@RestController
@RequestMapping("/setting")
public class SettingController {
    @Reference(version = "1.0", timeout = 10000)
    IUserSettingService iUserSettingService;

    @ApiOperation(value = "获取用户配置")
    @GetMapping
    public ResponseData getSetting() {
        GetSettingRequest getSettingRequest = new GetSettingRequest(PickerInfoHolder.getPickerInfo().getId());

        GetSettingResponse getSettingResponse = iUserSettingService.getSetting(getSettingRequest);

        return Optional.ofNullable(getSettingResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(getSettingResponse)
                        : new ResponseUtil<>().setErrorMsg(getSettingResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "修改用户配置")
    @PutMapping
    public ResponseData saveSetting(@RequestBody @Validated UserSettingForm form, BindingResult bindingResult) {
        ValidatorTool.check(bindingResult);

        SaveSettingRequest saveRequest = SaveSettingRequest.Builder.aSaveSettingRequest()
                .withMarkdownTheme(form.getMarkdownTheme())
                .withSafetyCheckMode(form.getSafetyCheckMode())
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .build();

        SaveSettingResponse settingResponse = iUserSettingService.saveSetting(saveRequest);

        return Optional.ofNullable(settingResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(null)
                        : new ResponseUtil<>().setErrorMsg(settingResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }
}
