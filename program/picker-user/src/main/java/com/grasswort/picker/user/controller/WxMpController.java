package com.grasswort.picker.user.controller;

import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.user.model.PickerInfoHolder;
import com.grasswort.picker.user.vo.QrcodeVO;
import com.grasswort.picker.wechat.IQrcodeService;
import com.grasswort.picker.wechat.constants.SysRetCodeConstants;
import com.grasswort.picker.wechat.dto.WxQrcodeRequest;
import com.grasswort.picker.wechat.dto.WxQrcodeResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
