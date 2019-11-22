package com.grasswort.picker.user.controller;

import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.commons.validator.ValidatorTool;
import com.grasswort.picker.user.IUserPrivateMpQrcodeService;
import com.grasswort.picker.user.dto.PrivateMpQrcodeRequest;
import com.grasswort.picker.user.dto.PrivateMpQrcodeResponse;
import com.grasswort.picker.user.dto.UploadPrivateMpQrcodeRequest;
import com.grasswort.picker.user.dto.UploadPrivateMpQrcodeResponse;
import com.grasswort.picker.user.model.PickerInfo;
import com.grasswort.picker.user.model.PickerInfoHolder;
import com.grasswort.picker.user.vo.QrcodeForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author xuliangliang
 * @Classname MpQrcodeController
 * @Description 微信公众号二维码服务
 * @Date 2019/11/22 21:21
 * @blame Java Team
 */
@Api(tags = "微信公众号推广")
@RestController
@RequestMapping("/mpQrcode")
public class MpQrcodeController {

    @Reference(version = "1.0", timeout = 10000)
    IUserPrivateMpQrcodeService iUserPrivateMpQrcodeService;

    @ApiOperation(value = "上传公众号二维码")
    @PostMapping
    public ResponseData uploadQrcode(@RequestBody @Validated QrcodeForm form, BindingResult bindingResult) {
        ValidatorTool.check(bindingResult);

        UploadPrivateMpQrcodeRequest uploadPrivateMpQrcodeRequest = UploadPrivateMpQrcodeRequest.Builder.anUploadPrivateMpQrcodeRequest()
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .withPrivateMpQrcode(form.getQrcode())
                .build();
        UploadPrivateMpQrcodeResponse uploadResponse = iUserPrivateMpQrcodeService.uploadPrivateMpQrcode(uploadPrivateMpQrcodeRequest);

        return Optional.ofNullable(uploadResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(null)
                        : new ResponseUtil<>().setErrorMsg(null)
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "获取公众号二维码")
    @GetMapping
    public ResponseData getQrcode() {
        PrivateMpQrcodeRequest mpQrcodeRequest = PrivateMpQrcodeRequest.Builder.aPrivateMpQrcodeRequest()
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .build();

        PrivateMpQrcodeResponse mpQrcodeResponse = iUserPrivateMpQrcodeService.getPrivateMpQrcode(mpQrcodeRequest);
        return Optional.ofNullable(mpQrcodeResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(mpQrcodeResponse.getQrcode())
                        : new ResponseUtil<>().setErrorMsg(mpQrcodeResponse.getQrcode())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }
}
