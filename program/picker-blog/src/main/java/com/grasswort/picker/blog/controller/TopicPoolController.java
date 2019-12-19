package com.grasswort.picker.blog.controller;

import com.grasswort.picker.blog.ITopicPoolService;
import com.grasswort.picker.blog.dto.TopicPoolRequest;
import com.grasswort.picker.blog.dto.TopicPoolResponse;
import com.grasswort.picker.blog.vo.TopicPoolForm;
import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.commons.validator.ValidatorTool;
import com.grasswort.picker.user.annotation.Anoymous;
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
                .build();
        TopicPoolResponse poolResponse = iTopicPoolService.topicPool(poolRequest);
        return Optional.ofNullable(poolResponse)
                .map(r -> r.isSuccess()
                    ? new ResponseUtil<>().setData(poolResponse.getTopics()).setTotal(poolResponse.getTotal())
                    : new ResponseUtil<>().setErrorMsg(poolResponse.getMsg())
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


