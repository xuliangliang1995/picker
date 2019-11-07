package com.grasswort.picker.blog.controller;

import com.grasswort.picker.blog.IBlogCategoryService;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.dto.*;
import com.grasswort.picker.blog.vo.CategoryTreeNodeVO;
import com.grasswort.picker.blog.vo.CreateCategoryForm;
import com.grasswort.picker.blog.vo.PatchCategoryForm;
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
import org.springframework.web.bind.annotation.*;

/**
 * @author xuliangliang
 * @Classname CategoryController
 * @Description 分类
 * @Date 2019/10/21 14:08
 * @blame Java Team
 */
@Api(tags = "类别")
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Reference(version = "1.0", timeout = 10000, cluster = ClusterFaultMechanism.FAIL_OVER)
    IBlogCategoryService iBlogCategoryService;

    @ApiOperation(value = "添加类别")
    @PostMapping
    public ResponseData addCategory(@RequestBody @Validated CreateCategoryForm form, BindingResult bindingResult) {
        ValidatorTool.check(bindingResult);

        CreateBlogCategoryRequest createRequest = CreateBlogCategoryRequest.Builder.aCreateBlogCategoryRequest()
                .withCategory(form.getCategory())
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .withParentId(form.getParentId())
                .build();
        CreateBlogCategoryResponse createResponse = iBlogCategoryService.createCategory(createRequest);

        if (createResponse != null && SysRetCodeConstants.SUCCESS.getCode().equals(createResponse.getCode())) {
            return new ResponseUtil<>().setData(null);
        }
        return new ResponseUtil<>().setErrorMsg(createResponse.getMsg());
    }

    @ApiOperation(value = "类别树")
    @GetMapping("/tree")
    public ResponseData<CategoryTreeNodeVO> categoryTree() {
        QueryBlogCategoryRequest queryRequest = new QueryBlogCategoryRequest();
        queryRequest.setUserId(PickerInfoHolder.getPickerInfo().getId());

        QueryBlogCategoryResponse queryResponse = iBlogCategoryService.categorys(queryRequest);
        if (queryResponse != null && SysRetCodeConstants.SUCCESS.getCode().equals(queryResponse.getCode())) {
            CategoryTreeNodeVO vo = new CategoryTreeNodeVO();
            vo.setNodes(queryResponse.getCategorys());
            return new ResponseUtil<CategoryTreeNodeVO>().setData(vo);
        }
        return new ResponseUtil<CategoryTreeNodeVO>().setErrorMsg(queryResponse.getMsg());
    }

    @ApiOperation(value = "修改分类")
    @PatchMapping("/{categoryId}")
    public ResponseData patchCategory(@RequestBody @Validated PatchCategoryForm form, BindingResult bindingResult, @PathVariable("categoryId") Long categoryId) {
        ValidatorTool.check(bindingResult);

        EditCategoryResponse editCategoryResponse = iBlogCategoryService.editCategory(
                EditCategoryRequest.Builder.anEditCategoryRequest()
                        .withCategoryId(categoryId)
                        .withParentId(form.getParentId())
                        .withCategory(form.getCategory())
                        .withUserId(PickerInfoHolder.getPickerInfo().getId())
                        .build()
        );

        if (null == editCategoryResponse) {
            return new ResponseUtil<CategoryTreeNodeVO>().setErrorMsg("系统异常");
        }

        if (SysRetCodeConstants.SUCCESS.getCode().equals(editCategoryResponse.getCode())) {
            return new ResponseUtil<>().setData(null);
        }

        return new ResponseUtil<>().setErrorMsg(editCategoryResponse.getMsg());
    }

    @ApiOperation(value = "删除分类")
    @DeleteMapping("/{categoryId}")
    public ResponseData deleteCategory(@PathVariable("categoryId") Long categoryId) {
        DeleteCategoryRequest deleteCategoryRequest = DeleteCategoryRequest.Builder.aDeleteCategoryRequest()
                .withCategoryId(categoryId)
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .build();

        DeleteCategoryResponse deleteCategoryResponse = iBlogCategoryService.deleteCategory(deleteCategoryRequest);

        if (SysRetCodeConstants.SUCCESS.getCode().equals(deleteCategoryResponse.getCode())) {
            return new ResponseUtil<>().setData(null);
        }

        return new ResponseUtil<>().setErrorMsg(deleteCategoryResponse.getMsg());
    }
}
