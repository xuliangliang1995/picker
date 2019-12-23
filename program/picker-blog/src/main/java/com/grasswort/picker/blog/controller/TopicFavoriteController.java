package com.grasswort.picker.blog.controller;

import com.grasswort.picker.blog.ITopicFavoriteService;
import com.grasswort.picker.blog.dto.TopicFavoriteCancelRequest;
import com.grasswort.picker.blog.dto.TopicFavoriteCancelResponse;
import com.grasswort.picker.blog.dto.TopicFavoriteRequest;
import com.grasswort.picker.blog.dto.TopicFavoriteResponse;
import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.user.model.PickerInfoHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author xuliangliang
 * @Classname TopicFavoriteController
 * @Description
 * @Date 2019/12/20 15:17
 * @blame Java Team
 */
@Api(tags = "专题收藏")
@RestController
@RequestMapping("/topic/{topicId}")
public class TopicFavoriteController {

    @Reference(version = "1.0", timeout = 10000)
    ITopicFavoriteService iTopicFavoriteService;

    @ApiOperation(value = "收藏")
    @PostMapping("/favorite")
    public ResponseData favorite(@PathVariable("topicId")String topicId) {
        TopicFavoriteRequest favoriteRequest = TopicFavoriteRequest.Builder.aTopicFavoriteRequest()
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .withTopicId(topicId)
                .build();
        TopicFavoriteResponse favoriteResponse = iTopicFavoriteService.topicFavorite(favoriteRequest);

        return Optional.ofNullable(favoriteResponse)
                .map(r -> r.isSuccess()
                    ? new ResponseUtil<>().setData(null)
                        : new ResponseUtil<>().setErrorMsg(favoriteResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "取消收藏")
    @DeleteMapping("/favorite")
    public ResponseData favoriteCancel(@PathVariable("topicId")String topicId) {
        TopicFavoriteCancelRequest favoriteCancelRequest = TopicFavoriteCancelRequest.Builder.aTopicFavoriteCancelRequest()
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .withTopicId(topicId)
                .build();
        TopicFavoriteCancelResponse favoriteCancelResponse = iTopicFavoriteService.topicFavoriteCancel(favoriteCancelRequest);

        return Optional.ofNullable(favoriteCancelResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(null)
                        : new ResponseUtil<>().setErrorMsg(favoriteCancelResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }
}
