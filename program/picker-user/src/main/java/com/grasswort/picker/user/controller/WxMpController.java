package com.grasswort.picker.user.controller;

import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.commons.validator.ValidatorTool;
import com.grasswort.picker.user.IUserBaseInfoService;
import com.grasswort.picker.user.annotation.Anoymous;
import com.grasswort.picker.user.dto.ChangeMpOpenIdRequest;
import com.grasswort.picker.user.dto.ChangeMpOpenIdResponse;
import com.grasswort.picker.user.model.PickerInfoHolder;
import com.grasswort.picker.user.vo.QrcodeVO;
import com.grasswort.picker.wechat.IQrcodeService;
import com.grasswort.picker.wechat.constants.SysRetCodeConstants;
import com.grasswort.picker.wechat.dto.WxQrcodeRequest;
import com.grasswort.picker.wechat.dto.WxQrcodeResponse;
import com.grasswort.picker.wechat.util.QrcodeCiperEncrypt;
import com.grasswort.picker.wechat.util.QrcodeInfo;
import com.grasswort.picker.wechat.vo.CallBackForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author xuliangliang
 * @Classname WxMpController
 * @Description 公众号相关接口
 * @Date 2019/11/14 20:05
 * @blame Java Team
 */
@Api(tags = "微信公众号相关接口")
@RestController
@RequestMapping("/wxmp")
public class WxMpController {

    @Reference(version = "1.0", timeout = 10000)
    IQrcodeService iQrcodeService;

    @Reference(version = "1.0", timeout = 10000)
    IUserBaseInfoService iUserBaseInfoService;

    @ApiOperation(value = "获取账号绑定二维码")
    @GetMapping("/bind/qrcode")
    public ResponseData<QrcodeVO> getQrcodeForBind() {
        WxQrcodeRequest wxQrcodeRequest = WxQrcodeRequest.Builder.aWxQrcodeRequest()
                .withScene("USER_BIND_WXMP")
                .withText(PickerInfoHolder.getPickerInfo().getId().toString())
                .withExpireSeconds(300)
                .withCallback("/api/user/wxmp/bind")
                .build();
        WxQrcodeResponse wxQrcodeResponse = iQrcodeService.generateQrcode(wxQrcodeRequest);
        if (SysRetCodeConstants.SUCCESS.getCode().equals(wxQrcodeResponse.getCode())) {
            QrcodeVO vo = new QrcodeVO(wxQrcodeResponse.getTicket(), wxQrcodeResponse.getUrl());
            return new ResponseUtil<QrcodeVO>().setData(vo);
        }
        return new ResponseUtil<QrcodeVO>().setErrorMsg(wxQrcodeResponse.getMsg());
    }


    @Anoymous
    @ApiOperation(value = "绑定 openId")
    @PostMapping("/bind")
    public ResponseData bindOpenId(@RequestBody @Validated CallBackForm form, BindingResult bindingResult) {
        ValidatorTool.check(bindingResult);
        String openId = form.getOpenId();
        QrcodeInfo qrcodeInfo = QrcodeCiperEncrypt.INSTANCE.decrypt(form.getBody());
        if (qrcodeInfo != null && NumberUtils.isDigits(qrcodeInfo.getText())) {
            ChangeMpOpenIdRequest changeMpOpenIdRequest = ChangeMpOpenIdRequest.Builder.aChangeMpOpenIdRequest()
                    .withUserId(Long.valueOf(qrcodeInfo.getText()))
                    .withOpenId(openId)
                    .build();
            ChangeMpOpenIdResponse changeMpOpenIdResponse = iUserBaseInfoService.changeOrBindMpOpenId(changeMpOpenIdRequest);
            if (SysRetCodeConstants.SUCCESS.getCode().equals(changeMpOpenIdResponse.getCode())) {
                return new ResponseUtil<>().setData(null);
            }
        }
        return new ResponseUtil<>().setErrorMsg("非法请求");
    }
}
