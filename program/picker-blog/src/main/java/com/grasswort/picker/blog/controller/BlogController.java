package com.grasswort.picker.blog.controller;

import com.grasswort.picker.blog.IBlogEditService;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.dto.CreateBlogRequest;
import com.grasswort.picker.blog.dto.CreateBlogResponse;
import com.grasswort.picker.blog.vo.CreateBlogForm;
import com.grasswort.picker.commons.constants.cluster.ClusterFaultMechanism;
import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.commons.validator.ValidatorTool;
import com.grasswort.picker.user.model.PickerInfoHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xuliangliang
 * @Classname BlogController
 * @Description 博客 API
 * @Date 2019/10/21 11:54
 * @blame Java Team
 */
@Api(tags = "博客")
@RestController
@RequestMapping
public class BlogController {

    @Reference(version = "1.0", cluster = ClusterFaultMechanism.FAIL_FAST)
    IBlogEditService iBlogEditService;

    @ApiOperation("创建博客")
    @PostMapping
    public ResponseData createBlog(@RequestBody @Validated CreateBlogForm form, BindingResult bindingResult) {
        ValidatorTool.check(bindingResult);

        CreateBlogRequest createBlogRequest = CreateBlogRequest.Builder.aCreateBlogRequest()
                .withMarkdown(form.getMarkdown())
                .withHtml(form.getHtml())
                .withCategoryId(form.getCategoryId())
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .build();
        CreateBlogResponse createBlogResponse = iBlogEditService.createBlog(createBlogRequest);

        if (SysRetCodeConstants.SUCCESS.getCode().equals(createBlogResponse.getCode())) {
            return new ResponseUtil<>().setData(null);
        }
        return new ResponseUtil<>().setErrorMsg(createBlogResponse.getMsg());
    }
}
