package com.grasswort.picker.blog.controller;

import com.grasswort.picker.blog.IBlogPoolService;
import com.grasswort.picker.blog.dto.BlogPoolQueryRequest;
import com.grasswort.picker.blog.dto.BlogPoolQueryResponse;
import com.grasswort.picker.blog.vo.BlogPoolForm;
import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.commons.validator.ValidatorTool;
import com.grasswort.picker.user.annotation.Anoymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author xuliangliang
 * @Classname BlogPoolController
 * @Description
 * @Date 2019/11/20 16:27
 * @blame Java Team
 */
@Api(tags = "博客池")
@RestController
@RequestMapping("/pool")
public class BlogPoolController {

    @Reference(version = "1.0", timeout = 10000)
    IBlogPoolService iBlogPoolService;

    @ApiOperation(value = "博客池")
    @Anoymous
    @GetMapping
    public ResponseData blogPool(@Validated BlogPoolForm form, BindingResult bindingResult) {
        ValidatorTool.check(bindingResult);

        BlogPoolQueryRequest queryRequest = BlogPoolQueryRequest.Builder.aBlogPoolQueryRequest()
                .withKeyword(form.getKeyword())
                .withPageNo(form.getPageNo())
                .withPageSize(form.getPageSize())
                .build();
        BlogPoolQueryResponse queryResponse = iBlogPoolService.blogPool(queryRequest);

        return Optional.ofNullable(queryResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(queryResponse.getBlogs())
                        : new ResponseUtil<>().setErrorMsg(queryResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }
}
