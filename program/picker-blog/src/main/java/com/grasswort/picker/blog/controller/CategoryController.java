package com.grasswort.picker.blog.controller;

import com.grasswort.picker.blog.IBlogCategoryService;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.dto.*;
import com.grasswort.picker.blog.vo.CategoryTreeNodeVO;
import com.grasswort.picker.blog.vo.CreateCategoryForm;
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
}
