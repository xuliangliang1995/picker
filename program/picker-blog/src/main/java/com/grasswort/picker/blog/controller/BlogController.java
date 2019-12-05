package com.grasswort.picker.blog.controller;

import com.grasswort.picker.blog.IBlogBrowseService;
import com.grasswort.picker.blog.IBlogEditService;
import com.grasswort.picker.blog.IBlogService;
import com.grasswort.picker.blog.IRetentionCurveService;
import com.grasswort.picker.blog.constant.BlogCurveStatusEnum;
import com.grasswort.picker.blog.dto.*;
import com.grasswort.picker.blog.vo.*;
import com.grasswort.picker.commons.constants.cluster.ClusterFaultMechanism;
import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.commons.validator.ValidatorTool;
import com.grasswort.picker.user.annotation.Anoymous;
import com.grasswort.picker.user.model.PickerInfoHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * @author xuliangliang
 * @Classname BlogController
 * @Description 博客 API
 * @Date 2019/10/21 11:54
 * @blame Java Team
 */
@Api(tags = "博客")
@RestController
@RequestMapping("/")
public class BlogController {

    @Reference(version = "1.0", cluster = ClusterFaultMechanism.FAIL_FAST)
    IBlogEditService iBlogEditService;

    @Reference(version = "1.0", cluster = ClusterFaultMechanism.FAIL_OVER)
    IBlogService iBlogService;

    @Reference(version = "1.0", cluster = ClusterFaultMechanism.FAIL_OVER)
    IRetentionCurveService iRetentionCurveService;

    @Reference(version = "1.0", cluster = ClusterFaultMechanism.FAIL_FAST)
    IBlogBrowseService iBlogBrowseService;

    @ApiOperation("创建博客")
    @PostMapping
    public ResponseData createBlog(@RequestBody @Validated CreateBlogForm form, BindingResult bindingResult) {
        ValidatorTool.check(bindingResult);

        CreateBlogRequest createBlogRequest = CreateBlogRequest.Builder.aCreateBlogRequest()
                .withTitle(form.getTitle())
                .withMarkdown(form.getMarkdown())
                .withHtml(form.getHtml())
                .withCategoryId(form.getCategoryId())
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .withCoverImg(form.getCoverImg())
                .withSummary(form.getSummary())
                .withLabels(form.getLabels())
                .build();
        CreateBlogResponse createBlogResponse = iBlogEditService.createBlog(createBlogRequest);

        return Optional.ofNullable(createBlogResponse)
                .map(r -> r.isSuccess()
                            ? new ResponseUtil<>().setData(null)
                            : new ResponseUtil<>().setErrorMsg(createBlogResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation("博客列表")
    @GetMapping
    public ResponseData listOwnBlog(@Validated OwnBlogListForm form, BindingResult bindingResult) {
        ValidatorTool.check(bindingResult);

        OwnBlogListRequest ownBlogListRequest = OwnBlogListRequest.Builder.anOwnBlogListRequest()
                .withCategoryId(form.getCategoryId())
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .withPageNo(form.getPageNo())
                .withPageSize(form.getPageSize())
                .build();
        OwnBlogListResponse ownBlogListResponse = iBlogService.ownBlogList(ownBlogListRequest);

        return Optional.ofNullable(ownBlogListResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(ownBlogListResponse.getBlogs()).setTotal(ownBlogListResponse.getTotal())
                        : new ResponseUtil<>().setErrorMsg(ownBlogListResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation("回收站")
    @GetMapping("/recycle/bin")
    public ResponseData recycleBin(@Validated RecycleBinForm recycleBinForm, BindingResult bindingResult) {
        ValidatorTool.check(bindingResult);
        RecyclableBlogRequest recyclableBlogRequest = RecyclableBlogRequest.Builder.aRecyclableBlogRequest()
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .withPageNo(recycleBinForm.getPageNo())
                .withPageSize(recycleBinForm.getPageSize())
                .build();
        RecyclableBlogResponse recyclableBlogResponse = iBlogService.recyclableBlogList(recyclableBlogRequest);

        return Optional.ofNullable(recyclableBlogResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(recyclableBlogResponse.getBlogs()).setTotal(recyclableBlogResponse.getTotal())
                        : new ResponseUtil<>().setErrorMsg(recyclableBlogResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @Anoymous
    @ApiOperation("获取博客 markdown")
    @GetMapping("/{blogId}/markdown")
    public ResponseData getBlogWithMarkdown(@PathVariable("blogId") String blogId) {

        BlogMarkdownRequest markdownRequest = new BlogMarkdownRequest();
        markdownRequest.setBlogId(blogId);

        BlogMarkdownResponse markdownResponse = iBlogService.markdown(markdownRequest);
        iBlogBrowseService.browse(BlogBrowseRequest.Builder.aBlogBrowseRequest().withBlogId(blogId).build());
        
        return Optional.ofNullable(markdownResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(markdownResponse.getBlog())
                        : new ResponseUtil<>().setErrorMsg(markdownResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @Anoymous
    @ApiOperation("获取博客 html")
    @GetMapping("/{blogId}.html")
    public ResponseEntity getBlogHtml(@PathVariable("blogId") String blogId) {
        BlogHtmlRequest htmlRequest = new BlogHtmlRequest();
        htmlRequest.setBlogId(blogId);

        BlogHtmlResponse htmlResponse = iBlogService.html(htmlRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/html;charset=utf-8"));;
        return new ResponseEntity<>(
                Optional.ofNullable(htmlResponse)
                        .map(BlogHtmlResponse::getHtml)
                        .orElse("系统错误"),
                headers, HttpStatus.OK);
    }

    @ApiOperation(value = "修改博客")
    @PutMapping("/{blogId}")
    public ResponseData editBlog(@RequestBody @Validated EditBlogForm form, BindingResult bindingResult, @PathVariable("blogId") String blogId) {
        ValidatorTool.check(bindingResult);

        EditBlogRequest editBlogRequest = EditBlogRequest.Builder.anEditBlogRequest()
                .withBlogId(blogId)
                .withTitle(form.getTitle())
                .withMarkdown(form.getMarkdown())
                .withHtml(form.getHtml())
                .withCategoryId(form.getCategoryId())
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .withCoverImg(form.getCoverImg())
                .withSummary(form.getSummary())
                .withLabels(form.getLabels())
                .build();

        EditBlogResponse editBlogResponse = iBlogEditService.editBlog(editBlogRequest);

        return Optional.ofNullable(editBlogResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(null)
                        : new ResponseUtil<>().setErrorMsg(editBlogResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "修改博客分类")
    @PatchMapping("/{blogId}/category")
    public ResponseData changeBlogCategory(@RequestBody @Validated ChangeBlogCategoryForm form, BindingResult bindingResult, @PathVariable("blogId") String blogId) {
        ValidatorTool.check(bindingResult);

        ChangeBlogCategoryResponse changeResponse = iBlogEditService.changeBlogCategory(
                ChangeBlogCategoryRequest.Builder.aChangeBlogCategoryRequest()
                .withBlogId(blogId)
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .withCategoryId(form.getCategoryId())
                .build()
        );

        return Optional.ofNullable(changeResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(null)
                        : new ResponseUtil<>().setErrorMsg(changeResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "删除博客")
    @DeleteMapping(("/{blogId}"))
    public ResponseData deleteBlog(@PathVariable("blogId") String blogId) {
        DeleteBlogRequest deleteBlogRequest = DeleteBlogRequest.Builder.aDeleteBlogRequest()
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .withBlogId(blogId)
                .build();

        DeleteBlogResponse deleteBlogResponse = iBlogEditService.deleteBlog(deleteBlogRequest);

        return Optional.ofNullable(deleteBlogResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(null)
                        : new ResponseUtil<>().setErrorMsg(deleteBlogResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "回收博客")
    @PatchMapping(("/{blogId}/recycle"))
    public ResponseData recycleBlog(@PathVariable("blogId") String blogId) {
        RecycleBlogRequest recycleBlogRequest = RecycleBlogRequest.Builder.aRecycleBlogRequest()
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .withBlogId(blogId)
                .build();

        RecycleBlogResponse recycleBlogResponse = iBlogEditService.recycleBlog(recycleBlogRequest);

        return Optional.ofNullable(recycleBlogResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(null)
                        : new ResponseUtil<>().setErrorMsg(recycleBlogResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "博客记忆曲线进度调整")
    @PatchMapping("/{blogId}/curve")
    public ResponseData adjustBlogRetentionCurve(
            @RequestBody @Validated BlogCurvePatchForm form, BindingResult bindingResult, @PathVariable("blogId") String blogId) {
        ValidatorTool.check(bindingResult);

        BlogCurveStatusEnum statusEnum = null;
        if (form.getStatus() != null) {
            statusEnum = Arrays.stream(BlogCurveStatusEnum.values())
                    .filter(c -> Objects.equals(c.status(), form.getStatus()))
                    .findFirst().get();
        }

        BlogCurveResponse blogCurveResponse = iRetentionCurveService.blogCurvePatch(
                BlogCurveRequest.Builder.aBlogCurveRequest()
                .withBlogId(blogId)
                .withOrder(form.getOrder())
                .withStatus(statusEnum)
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .build()
        );

        return Optional.ofNullable(blogCurveResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(null)
                        : new ResponseUtil<>().setErrorMsg(blogCurveResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

}
