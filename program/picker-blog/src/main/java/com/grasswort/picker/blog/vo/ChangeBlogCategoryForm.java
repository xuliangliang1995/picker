package com.grasswort.picker.blog.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname ChangeBlogCategoryForm
 * @Description 修改博客分类
 * @Date 2019/11/6 10:34
 * @blame Java Team
 */
@Data
public class ChangeBlogCategoryForm {
    @NotNull
    @Min(0)
    private Long categoryId;
}
