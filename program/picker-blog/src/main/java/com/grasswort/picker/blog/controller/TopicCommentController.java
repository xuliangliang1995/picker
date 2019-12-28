package com.grasswort.picker.blog.controller;

import com.grasswort.picker.blog.ITopicCommentService;
import com.grasswort.picker.blog.dto.TopicCommentRequest;
import com.grasswort.picker.blog.dto.TopicCommentResponse;
import com.grasswort.picker.blog.dto.TopicCommentsRequest;
import com.grasswort.picker.blog.dto.TopicCommentsResponse;
import com.grasswort.picker.blog.vo.AddTopicCommentForm;
import com.grasswort.picker.blog.vo.TopicCommentsForm;
import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.commons.validator.ValidatorTool;
import com.grasswort.picker.user.annotation.Anoymous;
import com.grasswort.picker.user.model.PickerInfo;
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
 * @Classname TopicCommentController
 * @Description
 * @Date 2019/12/23 15:45
 * @blame Java Team
 */
@Api(tags = "专题评分")
@RestController
@RequestMapping("/topic/{topicId}")
public class TopicCommentController {
    @Reference(version = "1.0", timeout = 10000)
    ITopicCommentService iTopicCommentService;

    @ApiOperation(value = "添加评分")
    @PostMapping("/comment")
    public ResponseData addComment(@RequestBody @Validated AddTopicCommentForm form, BindingResult bindingResult, @PathVariable("topicId") String topicId) {
        ValidatorTool.check(bindingResult);

        TopicCommentRequest commentRequest = TopicCommentRequest.Builder.aTopicCommentRequest()
                .withRate(form.getRate())
                .withContent(form.getContent())
                .withTopicId(topicId)
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .build();
        TopicCommentResponse commentResponse = iTopicCommentService.topicComment(commentRequest);
        return Optional.ofNullable(commentResponse)
                .map(r -> r.isSuccess()
                    ? new ResponseUtil<>().setData(null)
                        : new ResponseUtil<>().setErrorMsg(commentResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @Anoymous(resolve = true)
    @ApiOperation(value = "获取评分")
    @GetMapping("/comment")
    public ResponseData comments(@Validated TopicCommentsForm form, BindingResult bindingResult, @PathVariable("topicId") String topicId) {
        ValidatorTool.check(bindingResult);
        TopicCommentsRequest commentsRequest = TopicCommentsRequest.Builder.aTopicCommentsRequest()
                .withTopicId(topicId)
                .withUserId(
                        Optional.ofNullable(PickerInfoHolder.getPickerInfo()).map(PickerInfo::getId).orElse(null)
                ).withPageNo(form.getPageNo())
                .withPageSize(form.getPageSize())
                .build();
        TopicCommentsResponse commentsResponse = iTopicCommentService.topicComments(commentsRequest);
        return Optional.ofNullable(commentsResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(commentsResponse)
                        : new ResponseUtil<>().setErrorMsg(commentsResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }
}
