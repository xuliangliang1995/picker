package com.grasswort.picker.blog.controller;

import com.grasswort.picker.blog.IBlogTopicMenuService;
import com.grasswort.picker.blog.IBlogTopicService;
import com.grasswort.picker.blog.dto.*;
import com.grasswort.picker.blog.vo.MenuRenameForm;
import com.grasswort.picker.blog.vo.TopicForm;
import com.grasswort.picker.blog.vo.TopicListForm;
import com.grasswort.picker.blog.vo.TopicMenuCreateForm;
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

    @Reference(version = "1.0", timeout = 10000)
    IBlogTopicMenuService iBlogTopicMenuService;

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


    @ApiOperation(value = "创建菜单")
    @PostMapping("/{topicId}/menu")
    public ResponseData createTopicMenu(@RequestBody @Validated TopicMenuCreateForm menuCreateForm, BindingResult bindingResult, @PathVariable("topicId") String topicId) {
        ValidatorTool.check(bindingResult);

        TopicMenuCreateRequest menuCreateRequest = TopicMenuCreateRequest.Builder.aTopicMenuCreateRequest()
                .withTopicId(topicId)
                .withName(menuCreateForm.getName())
                .withType(menuCreateForm.getType())
                .withBlogId(menuCreateForm.getBlogId())
                .withParentMenuId(menuCreateForm.getParentMenuId())
                .withPkUserId(PickerInfoHolder.getPickerInfo().getId())
                .build();
        TopicMenuCreateResponse createResponse = iBlogTopicMenuService.createMenu(menuCreateRequest);
        return Optional.ofNullable(createResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(null)
                        : new ResponseUtil<>().setErrorMsg(createResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @Anoymous(resolve = true)
    @ApiOperation(value = "专题菜单")
    @GetMapping("/{topicId}/menu")
    public ResponseData topicMenu(@PathVariable("topicId") String topicId) {
        TopicMenuRequest menuRequest = TopicMenuRequest.Builder.aTopicMenuRequest()
                .withTopicId(topicId)
                .withPkUserId(
                        Optional.ofNullable(PickerInfoHolder.getPickerInfo())
                                .map(PickerInfo::getId)
                                .orElse(null)
                ).build();

        TopicMenuResponse menuResponse = iBlogTopicMenuService.topicMenu(menuRequest);
        return Optional.ofNullable(menuResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(menuResponse)
                        : new ResponseUtil<>().setErrorMsg(menuResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "删除专题菜单")
    @DeleteMapping("/{topicId}/menu/{menuId}")
    public ResponseData deleteMenu(@PathVariable("topicId") String topicId, @PathVariable("menuId") Long menuId) {
        DeleteTopicMenuRequest deleteRequest = DeleteTopicMenuRequest.Builder.aDeleteTopicMenuRequest()
                .withMenuId(menuId)
                .withTopicId(topicId)
                .withPkUserId(PickerInfoHolder.getPickerInfo().getId())
                .build();
        DeleteTopicMenuResponse deleteTopicMenuResponse = iBlogTopicMenuService.deleteTopicMenu(deleteRequest);
        return Optional.ofNullable(deleteTopicMenuResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(null)
                        : new ResponseUtil<>().setErrorMsg(deleteTopicMenuResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "重命名")
    @PatchMapping("/{topicId}/menu/{menuId}")
    public ResponseData rename(@RequestBody@Validated MenuRenameForm form, BindingResult bindingResult, @PathVariable("topicId") String topicId, @PathVariable("menuId") Long menuId) {
        ValidatorTool.check(bindingResult);

        RenameMenuRequest renameMenuRequest = RenameMenuRequest.Builder.aRenameMenuRequest()
                .withTopicId(topicId)
                .withMenuId(menuId)
                .withMenuName(form.getMenuName())
                .withPkUserId(PickerInfoHolder.getPickerInfo().getId())
                .build();
        RenameMenuResponse renameMenuResponse = iBlogTopicMenuService.rename(renameMenuRequest);
        return Optional.ofNullable(renameMenuResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(null)
                        : new ResponseUtil<>().setErrorMsg(renameMenuResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "上移")
    @PatchMapping("/{topicId}/menu/{menuId}/up")
    public ResponseData moveUpMenu(@PathVariable("topicId") String topicId, @PathVariable("menuId") Long menuId) {
        TopicMenuMoveUpRequest moveUpRequest = TopicMenuMoveUpRequest.Builder.aTopicMenuMoveUpRequest()
                .withMenuId(menuId)
                .withTopicId(topicId)
                .withPkUserId(PickerInfoHolder.getPickerInfo().getId())
                .build();
        TopicMenuMoveUpResponse moveUpResponse = iBlogTopicMenuService.moveUp(moveUpRequest);
        return Optional.ofNullable(moveUpResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(null)
                        : new ResponseUtil<>().setErrorMsg(moveUpResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "下移")
    @PatchMapping("/{topicId}/menu/{menuId}/down")
    public ResponseData moveDownMenu(@PathVariable("topicId") String topicId, @PathVariable("menuId") Long menuId) {
        TopicMenuMoveDownRequest moveDownRequest = TopicMenuMoveDownRequest.Builder.aTopicMenuMoveDownRequest()
                .withTopicId(topicId)
                .withMenuId(menuId)
                .withTopicId(topicId)
                .build();
        TopicMenuMoveDownResponse moveDownResponse = iBlogTopicMenuService.moveDown(moveDownRequest);
        return Optional.ofNullable(moveDownResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(null)
                        : new ResponseUtil<>().setErrorMsg(moveDownResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }
}
