package com.grasswort.picker.blog.controller;

import com.grasswort.picker.blog.IBlogTopicService;
import com.grasswort.picker.blog.dto.*;
import com.grasswort.picker.blog.vo.TopicForm;
import com.grasswort.picker.blog.vo.TopicListForm;
import com.grasswort.picker.blog.vo.TopicStatusForm;
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
 * @Classname TopicController
 * @Description 专题
 * @Date 2019/12/10 16:12
 * @blame Java Team
 */
@Api(tags = "专题")
@RestController
@RequestMapping("/topic")
public class TopicController {

    @Reference(version = "1.0", timeout = 10000)
    IBlogTopicService iBlogTopicService;

    @ApiOperation(value = "专题列表")
    @GetMapping
    public ResponseData topics(@Validated TopicListForm listForm, BindingResult bindingResult) {
        ValidatorTool.check(bindingResult);

        MyTopicListRequest myTopicListRequest = MyTopicListRequest.Builder.aMyTopicListRequest()
                .withPageNo(listForm.getPageNo())
                .withPageSize(listForm.getPageSize())
                .withPkUserId(PickerInfoHolder.getPickerInfo().getId())
                .build();
        MyTopicListResponse myTopicListResponse = iBlogTopicService.topics(myTopicListRequest);
        return Optional.ofNullable(myTopicListResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(myTopicListResponse.getTopics()).setTotal(myTopicListResponse.getTotal())
                        : new ResponseUtil<>().setErrorMsg(myTopicListResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "创建专题")
    @PostMapping
    public ResponseData createTopic(@RequestBody @Validated TopicForm topicForm, BindingResult bindingResult) {
        ValidatorTool.check(bindingResult);

        TopicCreateRequest createRequest = TopicCreateRequest.Builder.aTopicCreateRequest()
                .withTitle(topicForm.getTitle())
                .withSummary(topicForm.getSummary())
                .withCoverImg(topicForm.getCoverImg())
                .withPkUserId(PickerInfoHolder.getPickerInfo().getId())
                .build();
        TopicCreateResponse createResponse = iBlogTopicService.createTopic(createRequest);

        return Optional.ofNullable(createResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(null)
                        : new ResponseUtil<>().setErrorMsg(createResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "编辑专题")
    @PutMapping("/{topicId}")
    public ResponseData editTopic(@RequestBody @Validated TopicForm topicForm, BindingResult bindingResult, @PathVariable("topicId") String topicId) {
        ValidatorTool.check(bindingResult);

        TopicEditRequest editRequest = TopicEditRequest.Builder.aTopicEditRequest()
                .withTopicId(topicId)
                .withPkUserId(PickerInfoHolder.getPickerInfo().getId())
                .withTitle(topicForm.getTitle())
                .withSummary(topicForm.getSummary())
                .withCoverImg(topicForm.getCoverImg())
                .build();
        TopicEditResponse editResponse = iBlogTopicService.editTopic(editRequest);
        return Optional.ofNullable(editResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(null)
                        : new ResponseUtil<>().setErrorMsg(editResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "删除专题")
    @DeleteMapping("/{topicId}")
    public ResponseData deleteTopic(@PathVariable("topicId") String topicId) {
        if (! PickerInfoHolder.getPickerInfo().isPrivilege()) {
            return new ResponseUtil<>().setErrorMsg("权限不足");
        }
        TopicDeleteRequest deleteRequest = TopicDeleteRequest.Builder.aTopicDeleteRequest()
                .withTopicId(topicId)
                .withPkUserId(PickerInfoHolder.getPickerInfo().getId())
                .build();
        TopicDeleteResponse deleteResponse = iBlogTopicService.deleteTopic(deleteRequest);
        return Optional.ofNullable(deleteResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(null)
                        : new ResponseUtil<>().setErrorMsg(deleteResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "修改状态")
    @PatchMapping("/{topicId}/status")
    public ResponseData changeTopicStatus(@RequestBody @Validated TopicStatusForm form, BindingResult bindingResult, @PathVariable("topicId") String topicId) {
        ValidatorTool.check(bindingResult);

        TopicStatusChangeRequest changeRequest = TopicStatusChangeRequest.Builder.aTopicStatusChangeRequest()
                .withTopicId(topicId)
                .withPkUserId(PickerInfoHolder.getPickerInfo().getId())
                .withStatus(form.getStatus())
                .build();
        TopicStatusChangeResponse changeResponse = iBlogTopicService.changeStatus(changeRequest);
        return Optional.ofNullable(changeResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(null)
                        : new ResponseUtil<>().setErrorMsg(changeResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }


}
