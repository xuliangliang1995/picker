package com.grasswort.picker.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.grasswort.picker.commons.constants.TOrF;
import com.grasswort.picker.commons.ip.PickerIpUtil;
import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.commons.validator.ValidatorTool;
import com.grasswort.picker.user.IUserPrivilegeService;
import com.grasswort.picker.user.constants.JwtTokenConstants;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dto.UserPrivilegeRequest;
import com.grasswort.picker.user.dto.UserPrivilegeResponse;
import com.grasswort.picker.user.model.PickerInfoHolder;
import com.grasswort.picker.user.vo.CaptchaUpgradePrivilegeForm;
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
 * @Classname UserPrivilegeController
 * @Description 提权接口
 * @Date 2019/10/12 15:55
 * @blame Java Team
 */
@Api(tags = "用户临时提权")
@RestController
@RequestMapping("/privilege")
public class UserPrivilegeController {
    @Reference(version = "1.0", timeout = 10000, validation = TOrF.FALSE)
    IUserPrivilegeService iUserPrivilegeService;

    @ApiOperation("通过验证码进行身份验证并提权")
    @PostMapping("/captcha")
    public ResponseData upgradePrivilegeByCaptcha(
            @RequestBody @Validated CaptchaUpgradePrivilegeForm privilegeForm, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        ValidatorTool.check(result);

        UserPrivilegeRequest privilegeRequest = UserPrivilegeRequest.Builder.anUserPrivilegeRequest()
                .withCaptch(privilegeForm.getCaptcha())
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .withIp(PickerIpUtil.getIp(request))
                .build();

        UserPrivilegeResponse privilegeResponse = iUserPrivilegeService.upgradePrivilege(privilegeRequest);

        return Optional.ofNullable(privilegeResponse)
                .map(r -> {
                    if (r.isSuccess()) {
                        response.setHeader(JwtTokenConstants.JWT_ACCESS_TOKEN_KEY, privilegeResponse.getAccessToken());
                        return new ResponseUtil<>().setData(null);
                    }
                    return new ResponseUtil<>().setErrorMsg(privilegeResponse.getMsg());
                })
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation("客户端查看当前持有 access_token 是否具备高级权限")
    @GetMapping
    public ResponseData queryTokenPrivilege() {
        JSONObject result = new JSONObject();
        result.put("privilege", PickerInfoHolder.getPickerInfo().isPrivilege());
        return new ResponseUtil<>().setData(result);
    }
}
