package com.grasswort.picker.user.controller;

import com.grasswort.picker.commons.constants.TOrF;
import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.commons.validator.ValidatorTool;
import com.grasswort.picker.user.IUserActivateService;
import com.grasswort.picker.user.annotation.Anoymous;
import com.grasswort.picker.user.dto.*;
import com.grasswort.picker.user.vo.ActivateStatusVO;
import com.grasswort.picker.user.vo.QueryActivateStatusForm;
import com.grasswort.picker.user.vo.SendActivateEmailForm;
import com.grasswort.picker.user.vo.UserActivateForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author xuliangliang
 * @Classname UserActivateController
 * @Description 用户激活
 * @Date 2019/10/7 13:04
 * @blame Java Team
 */
@Api(tags = "Picker 账号激活")
@Anoymous
@RestController
@RequestMapping("/activate")
public class ActivateController {

    @Reference(version = "1.0", timeout = 10000, validation = TOrF.TRUE)
    IUserActivateService iUserActivateService;

    @ApiOperation(value = "获取账号激活状态")
    @GetMapping("/status")
    public ResponseData<ActivateStatusVO> activateStatus(@Validated QueryActivateStatusForm form, BindingResult bindingResult) {
        ValidatorTool.check(bindingResult);

        QueryActivateStatusRequest activateStatusRequest = QueryActivateStatusRequest.Builder.aQueryActivateStatusRequest()
                .withUsername(form.getUsername())
                .build();

        QueryActivateStatusResponse activateStatusResponse = iUserActivateService.activateStatus(activateStatusRequest);

        return Optional.ofNullable(activateStatusResponse)
                .map(r -> {
                    if (r.isSuccess()) {
                        ActivateStatusVO vo = new ActivateStatusVO();
                        vo.setActivated(activateStatusResponse.isActivated());
                        vo.setEmail(activateStatusResponse.getEmail());
                        return new ResponseUtil<ActivateStatusVO>().setData(vo);
                    }
                    return new ResponseUtil<ActivateStatusVO>().setErrorMsg(activateStatusResponse.getMsg());
                })
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    /**
     * 激活邮件
     * @param form
     * @return ResponseData
     */
    @ApiOperation(value = "发送激活邮件")
    @PostMapping("/email")
    public ResponseData activateEmail(@RequestBody @Validated SendActivateEmailForm form, BindingResult bindingResult) {
        ValidatorTool.check(bindingResult);

        SendActivateEmailRequest emailRequest = SendActivateEmailRequest.Builder.aSendActivateEmailRequest()
                .withUsername(form.getUsername())
                .build();

        SendActivateEmailResponse sendActivateEmailResponse = iUserActivateService.sendActivateEmail(emailRequest);

        return Optional.ofNullable(sendActivateEmailResponse)
                .map(r -> r.isSuccess()
                            ? new ResponseUtil().setData(null)
                            : new ResponseUtil<>().setErrorMsg(sendActivateEmailResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    /**
     * 激活
     * @param activateForm
     * @return
     */
    @ApiOperation(value = "账号激活（通过邮箱链接）")
    @GetMapping
    public ResponseData activate(@Validated UserActivateForm activateForm, BindingResult bindingResult) {
        ValidatorTool.check(bindingResult);

        UserActivateRequest activateRequest = UserActivateRequest.Builder.anUserActivateRequest()
                .withUsername(activateForm.getUsername())
                .withActivationCode(activateForm.getCode())
                .withActivateId(activateForm.getActivateId())
                .build();

        UserActivateResponse activateResponse = iUserActivateService.activate(activateRequest);

        return Optional.ofNullable(activateResponse)
                .map(r -> r.isSuccess()
                            ? new ResponseUtil().setData(null)
                            : new ResponseUtil().setErrorMsg(activateResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }


}
