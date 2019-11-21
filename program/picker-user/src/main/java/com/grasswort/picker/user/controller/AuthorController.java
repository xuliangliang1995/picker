package com.grasswort.picker.user.controller;

import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.user.IUserBaseInfoService;
import com.grasswort.picker.user.annotation.Anoymous;
import com.grasswort.picker.user.dto.UserBaseInfoRequest;
import com.grasswort.picker.user.dto.UserBaseInfoResponse;
import com.grasswort.picker.user.util.PickerIdEncrypt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author xuliangliang
 * @Classname AuthorController
 * @Description 作者（查看其它用户相关接口）
 * @Date 2019/11/21 11:04
 * @blame Java Team
 */
@Api(tags = "作者")
@RestController
@RequestMapping("/author")
public class AuthorController {

    @Reference(version = "1.0", timeout = 10000)
    IUserBaseInfoService iUserBaseInfoService;

    @Anoymous
    @ApiOperation(value = "获取作者信息")
    @GetMapping("/{pickerID}")
    public ResponseData authorInfo(@PathVariable("pickerID")String pickerID) {
        Long pickerId = PickerIdEncrypt.decrypt(pickerID);
        if (pickerId == null) {
            return new ResponseUtil<>().setErrorMsg("访问用户不存在！");
        }
        UserBaseInfoRequest baseInfoRequest = UserBaseInfoRequest.Builder.anUserBaseInfoRequest()
                .withUserId(pickerId)
                .build();
        UserBaseInfoResponse baseInfoResponse = iUserBaseInfoService.userBaseInfo(baseInfoRequest);
        return Optional.ofNullable(baseInfoResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(baseInfoResponse)
                        : new ResponseUtil<>().setErrorMsg(baseInfoResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }


}
