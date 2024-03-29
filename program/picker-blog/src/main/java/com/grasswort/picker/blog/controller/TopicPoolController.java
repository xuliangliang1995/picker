package com.grasswort.picker.blog.controller;

import com.grasswort.picker.blog.ITopicFavoriteService;
import com.grasswort.picker.blog.ITopicPoolService;
import com.grasswort.picker.blog.dto.TopicFavoriteListRequest;
import com.grasswort.picker.blog.dto.TopicFavoriteListResponse;
import com.grasswort.picker.blog.dto.TopicPoolRequest;
import com.grasswort.picker.blog.dto.TopicPoolResponse;
import com.grasswort.picker.blog.vo.TopicFavoriteListForm;
import com.grasswort.picker.blog.vo.TopicPoolForm;
import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.commons.validator.ValidatorTool;
import com.grasswort.picker.user.annotation.Anoymous;
import com.grasswort.picker.user.model.PickerInfo;
import com.grasswort.picker.user.model.PickerInfoHolder;
import com.grasswort.picker.user.util.PickerIdEncrypt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author xuliangliang
 * @Classname TopicPoolController
 * @Description
 * @Date 2019/12/19 14:51
 * @blame Java Team
 */
@Api(tags = "专题池")
@Anoymous
@RestController
@RequestMapping("/topic/pool")
public class TopicPoolController {

    @Reference(version = "1.0", timeout = 10000)
    ITopicPoolService iTopicPoolService;

    @Reference(version = "1.0", timeout = 10000)
    ITopicFavoriteService iTopicFavoriteService;

    @Anoymous(resolve = true)
    @ApiOperation(value = "专题池")
    @GetMapping
    public ResponseData topicPool(@Validated TopicPoolForm form, BindingResult bindingResult) {
        ValidatorTool.check(bindingResult);
        String authorId = form.getAuthorId();
        String keyword = form.getKeyword();

        TopicPoolRequest poolRequest = TopicPoolRequest.Builder.aTopicPoolRequest()
                .withAuthorId(PickerIdEncrypt.decrypt(authorId))
                .withKeyword(keyword)
                .withPageNo(form.getPageNo())
                .withPageSize(form.getPageSize())
                .withBrowseUserId(
                        Optional.ofNullable(PickerInfoHolder.getPickerInfo()).map(PickerInfo::getId).orElse(null)
                )
                .build();
        TopicPoolResponse poolResponse = iTopicPoolService.topicPool(poolRequest);
        return Optional.ofNullable(poolResponse)
                .map(r -> r.isSuccess()
                    ? new ResponseUtil<>().setData(poolResponse.getTopics()).setTotal(poolResponse.getTotal())
                    : new ResponseUtil<>().setErrorMsg(poolResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @Anoymous(resolve = true)
    @ApiOperation(value = "收藏列表")
    @GetMapping(value = "/favorite")
    public ResponseData topicFavoritePool(@Validated TopicFavoriteListForm form, BindingResult bindingResult) {
        ValidatorTool.check(bindingResult);

        TopicFavoriteListRequest favoriteListRequest = TopicFavoriteListRequest.Builder.aTopicFavoriteListRequest()
                .withAuthorId(PickerIdEncrypt.decrypt(form.getAuthorId()))
                .withPageNo(form.getPageNo())
                .withPageSize(form.getPageSize())
                .withPickerId(
                        Optional.ofNullable(PickerInfoHolder.getPickerInfo()).map(PickerInfo::getId).orElse(null)
                ).build();
        TopicFavoriteListResponse favoriteListResponse = iTopicFavoriteService.listTopicFavorite(favoriteListRequest);
        return Optional.ofNullable(favoriteListResponse)
                .map(r -> r.isSuccess()
                    ? new ResponseUtil<>().setData(favoriteListResponse.getTopics()).setTotal(favoriteListResponse.getTotal())
                    : new ResponseUtil<>().setErrorMsg(favoriteListResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "初始化")
    @PostMapping
    public ResponseData init() {
        iTopicPoolService.init();
        return new ResponseUtil().setData(null);
    }
}


