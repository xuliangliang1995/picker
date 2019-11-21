package com.grasswort.picker.user.controller;

import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.user.IUserSubscribeService;
import com.grasswort.picker.user.dto.*;
import com.grasswort.picker.user.model.PickerInfoHolder;
import com.grasswort.picker.user.util.PickerIdEncrypt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/subscribe/author")
public class SubscribeAuthorController {

    @Reference(version = "1.0", timeout = 10000)
    IUserSubscribeService iUserSubscribeService;

    @ApiOperation(value = "关注")
    @PostMapping("/{pickerID}")
    public ResponseData subscribeAuthor(@PathVariable("pickerID")String pickerID) {
        Long authorId = PickerIdEncrypt.decrypt(pickerID);
        if (authorId == null) {
            return new ResponseUtil<>().setErrorMsg("关注用户不存在！");
        }

        SubscribeRequest subscribeRequest = SubscribeRequest.Builder.aSubscribeRequest()
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .withAuthorId(authorId)
                .build();
        SubscribeResponse subscribeResponse = iUserSubscribeService.subscribe(subscribeRequest);
        return Optional.ofNullable(subscribeResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(null)
                        : new ResponseUtil<>().setErrorMsg(subscribeResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "取消关注")
    @DeleteMapping("/{pickerID}")
    public ResponseData unsubscribeAuthor(@PathVariable("pickerID")String pickerID) {
        Long authorId = PickerIdEncrypt.decrypt(pickerID);
        if (authorId == null) {
            return new ResponseUtil<>().setData(null);
        }

        UnsubscribeRequest unsubscribeRequest = UnsubscribeRequest.Builder.anUnsubscribeRequest()
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .withAuthorId(authorId)
                .build();
        UnsubscribeResponse unsubscribeResponse = iUserSubscribeService.unsubscribe(unsubscribeRequest);
        return Optional.ofNullable(unsubscribeResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(null)
                        : new ResponseUtil<>().setErrorMsg(unsubscribeResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "关注状态")
    @GetMapping("/{pickerID}/status")
    public ResponseData subscribeStatus(@PathVariable("pickerID")String pickerID) {

        Long authorId = PickerIdEncrypt.decrypt(pickerID);
        if (authorId == null) {
            return new ResponseUtil<>().setData(false);
        }

        SubscribeStatusRequest subscribeStatusRequest = SubscribeStatusRequest.Builder.aSubscribeStatusRequest()
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .withAuthorId(authorId)
                .build();
        SubscribeStatusResponse subscribeStatusResponse = iUserSubscribeService.subscribeStatus(subscribeStatusRequest);
        return Optional.ofNullable(subscribeStatusResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(subscribeStatusResponse.isSubscribe())
                        : new ResponseUtil<>().setErrorMsg(subscribeStatusResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

}
