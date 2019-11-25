package com.grasswort.picker.blog.controller;

import com.grasswort.picker.blog.IBlogLikeService;
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
 * @Classname BlogLikeController
 * @Description 点赞、取消点赞
 * @Date 2019/11/25 14:36
 * @blame Java Team
 */
@Api(tags = "点赞")
@RestController
@RequestMapping("/{blogId}/like")
public class BlogLikeController {

    @Reference(version = "1.0", timeout = 10000)
    IBlogLikeService iBlogLikeService;

    @ApiOperation(value = "点赞状态")
    @GetMapping
    public ResponseData likeStatus(@PathVariable("blogId") String blogId) {
        BlogLikeStatusRequest likeStatusRequest = BlogLikeStatusRequest.Builder.aBlogLikeStatusRequest()
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .withBlogId(blogId)
                .build();
        BlogLikeStatusResponse likeStatusResponse = iBlogLikeService.blogLikeStatus(likeStatusRequest);
        return Optional.ofNullable(likeStatusResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(r.getLike())
                        : new ResponseUtil<>().setErrorMsg(r.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "点赞")
    @PostMapping
    public ResponseData like(@PathVariable("blogId") String blogId) {
        BlogLikeRequest likeRequest = BlogLikeRequest.Builder.aBlogLikeRequest()
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .withBlogId(blogId)
                .build();

        BlogLikeResponse likeResponse = iBlogLikeService.blogLike(likeRequest);

        return Optional.ofNullable(likeResponse)
                .map(l -> l.isSuccess()
                        ? new ResponseUtil<>().setData(null)
                        : new ResponseUtil<>().setErrorMsg(likeResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "点赞取消")
    @DeleteMapping
    public ResponseData likeCancel(@PathVariable("blogId") String blogId) {
        BlogLikeCancelRequest likeCancelRequest = BlogLikeCancelRequest.Builder.aBlogLikeCancelRequest()
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .withBlogId(blogId)
                .build();

        BlogLikeCancelResponse likeCancelResponse = iBlogLikeService.blogLikeCancel(likeCancelRequest);

        return Optional.ofNullable(likeCancelResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(null)
                        : new ResponseUtil<>().setErrorMsg(likeCancelResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }
}
