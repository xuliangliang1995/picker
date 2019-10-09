package com.grasswort.picker.user.controller;

import com.grasswort.picker.commons.constants.TOrF;
import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.commons.validator.ValidatorTool;
import com.grasswort.picker.user.IUserRegisterService;
import com.grasswort.picker.user.annotation.Anoymous;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dto.UserRegisterRequest;
import com.grasswort.picker.user.dto.UserRegisterResponse;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xuliangliang
 * @Classname RegisterController
 * @Description 注册
 * @Date 2019/9/22 10:37
 * @blame Java Team
 */
@Anoymous
@RestController
@RequestMapping("/user")
public class RegisterController {

    @Reference(timeout = 2000, version = "1.0", validation = TOrF.TRUE, mock = TOrF.TRUE)
    IUserRegisterService iUserRegisterService;


    @PostMapping("/register")
    public ResponseData register(@RequestBody @Validated UserRegisterRequest body, BindingResult bindingResult) {
        ValidatorTool.check(bindingResult);
        UserRegisterResponse result = iUserRegisterService.register(body);
        if (result.getCode().equals(SysRetCodeConstants.SUCCESS.getCode())) {
            return new ResponseUtil<>().setData(null);
        }
        return new ResponseUtil<>().setErrorMsg(result.getMsg());
    }

}
