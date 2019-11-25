package com.grasswort.picker.blog.controller;

import com.grasswort.picker.blog.IBlogFavoriteService;
import com.grasswort.picker.blog.dto.*;
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
 * @Classname BlogFavoriteController
 * @Description 收藏、取消收藏
 * @Date 2019/11/25 14:43
 * @blame Java Team
 */
@Api(tags = "收藏")
@RestController
@RequestMapping("/{blogId}/favorite")
public class BlogFavoriteController {

    @Reference(version = "1.0", timeout = 10000)
    IBlogFavoriteService iBlogFavoriteService;

    @GetMapping
    @ApiOperation(value = "收藏状态")
    public ResponseData blogFavoriteStatus(@PathVariable("blogId") String blogId) {
        BlogFavoriteStatusRequest favoriteStatusRequest = BlogFavoriteStatusRequest.Builder.aBlogFavoriteStatusRequest()
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .withBlogId(blogId)
                .build();
        BlogFavoriteStatusResponse favoriteStatusResponse = iBlogFavoriteService.blogFavoriteStatus(favoriteStatusRequest);
        return Optional.ofNullable(favoriteStatusResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(r.getFavorite())
                        : new ResponseUtil<>().setErrorMsg(r.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "收藏")
    @PostMapping
    public ResponseData blogFavorite(@PathVariable("blogId") String blogId) {
        BlogFavoriteRequest favoriteRequest = BlogFavoriteRequest.Builder.aBlogFavoriteRequest()
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .withBlogId(blogId)
                .build();
        BlogFavoriteResponse favoriteResponse = iBlogFavoriteService.blogFavorite(favoriteRequest);
        return Optional.ofNullable(favoriteResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(null)
                        : new ResponseUtil<>().setErrorMsg(favoriteResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "取消收藏")
    @DeleteMapping
    public ResponseData blogFavoriteCancel(@PathVariable("blogId") String blogId) {
        BlogFavoriteCancelRequest favoriteCancelRequest = BlogFavoriteCancelRequest.Builder.aBlogFavoriteCancelRequest()
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .withBlogId(blogId)
                .build();

        BlogFavoriteCancelResponse favoriteCancelResponse = iBlogFavoriteService.blogFavoriteCancel(favoriteCancelRequest);
        return Optional.ofNullable(favoriteCancelResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(null)
                        : new ResponseUtil<>().setErrorMsg(favoriteCancelResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }
}
