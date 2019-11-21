package com.grasswort.picker.blog.controller;

import com.grasswort.picker.blog.IBlogCommentService;
import com.grasswort.picker.blog.dto.AddCommentRequest;
import com.grasswort.picker.blog.dto.AddCommentResponse;
import com.grasswort.picker.blog.dto.BlogCommentRequest;
import com.grasswort.picker.blog.dto.BlogCommentResponse;
import com.grasswort.picker.blog.vo.CommentForm;
import com.grasswort.picker.blog.vo.GetCommentForm;
import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.commons.validator.ValidatorTool;
import com.grasswort.picker.user.model.PickerInfoHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author xuliangliang
 * @Classname CommentController
 * @Description 评论
 * @Date 2019/11/21 23:06
 * @blame Java Team
 */
@Api(tags = "评论")
@RestController
@RequestMapping("/{blogId}/comment")
public class CommentController {

    @Reference(version = "1.0", timeout = 10000)
    IBlogCommentService iBlogCommentService;

    @ApiOperation(value = "添加评论")
    @PostMapping
    public ResponseData addComment(@RequestBody @Validated CommentForm form, BindingResult bindingResult, @PathVariable("blogId") String blogId) {
        ValidatorTool.check(bindingResult);
        AddCommentRequest addCommentRequest = AddCommentRequest.Builder.anAddCommentRequest()
                .withBlogId(blogId)
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .withContent(form.getContent())
                .withReplyCommentId(form.getReplyCommentId())
                .build();
        AddCommentResponse addCommentResponse = iBlogCommentService.addComment(addCommentRequest);
        return Optional.ofNullable(addCommentResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(null)
                        : new ResponseUtil<>().setErrorMsg(addCommentResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "获取评论")
    @GetMapping
    public ResponseData getComments(@Validated GetCommentForm form, BindingResult bindingResult, @PathVariable("blogId") String blogId) {
        ValidatorTool.check(bindingResult);

        BlogCommentRequest commentRequest = BlogCommentRequest.Builder.aBlogCommentRequest()
                .withBlogId(blogId)
                .withPageNo(form.getPageNo())
                .withPageSize(form.getPageSize())
                .build();
        BlogCommentResponse commentResponse = iBlogCommentService.comments(commentRequest);
        return Optional.ofNullable(commentResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(commentResponse.getComments()).setTotal(commentResponse.getTotal())
                        : new ResponseUtil<>().setErrorMsg(commentResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }
}
