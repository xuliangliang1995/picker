package com.grasswort.picker.user.controller;

import com.grasswort.picker.commons.constants.TOrF;
import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.user.IUserActivateService;
import com.grasswort.picker.user.annotation.Anoymous;
import com.grasswort.picker.user.constants.ActivateUrlConstants;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dto.SendActivateEmailRequest;
import com.grasswort.picker.user.dto.SendActivateEmailResponse;
import com.grasswort.picker.user.dto.UserActivateRequest;
import com.grasswort.picker.user.dto.UserActivateResponse;
import com.grasswort.picker.user.vo.UserActivateForm;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

/**
 * @author xuliangliang
 * @Classname UserActivateController
 * @Description 用户激活
 * @Date 2019/10/7 13:04
 * @blame Java Team
 */
@RestController
@RequestMapping(ActivateUrlConstants.PATH)
public class UserActivateController {

    @Reference(version = "1.0", timeout = 2000, validation = TOrF.TRUE)
    IUserActivateService iUserActivateService;

    /**
     * 发送激活邮件
     * @param username
     * @return
     */
    @Anoymous
    @GetMapping("/email")
    public ResponseData activateEmail(
            @RequestParam("username") String username) {
        SendActivateEmailRequest emailRequest = new SendActivateEmailRequest();
        emailRequest.setUsername(username);
        SendActivateEmailResponse result = iUserActivateService.sendActivateEmail(emailRequest);
        if (SysRetCodeConstants.SUCCESS.getCode().equals(result.getCode())) {
            return new ResponseUtil().setData(result.getEmail());
        }
        return new ResponseUtil<>().setErrorMsg(result.getMsg());
    }

    /**
     * 激活
     * @param activateForm
     * @return
     */
    @Anoymous
    @GetMapping
    public ResponseData activate(UserActivateForm activateForm) {
        UserActivateRequest activateRequest = new UserActivateRequest();
        activateRequest.setUsername(activateForm.getUsername());
        activateRequest.setActivationCode(activateForm.getCode());
        activateRequest.setActivateId(activateForm.getActivateId());
        UserActivateResponse activateResponse = iUserActivateService.activate(activateRequest);
        if (activateResponse.getCode().equals(SysRetCodeConstants.SUCCESS.getCode())) {
            return new ResponseUtil().setData(null, "Picker 账户激活成功");
        }
        return new ResponseUtil().setErrorMsg(activateResponse.getMsg());
    }


}
