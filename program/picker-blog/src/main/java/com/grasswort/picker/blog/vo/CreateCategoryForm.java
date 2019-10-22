package com.grasswort.picker.blog.vo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;

/**
 * @author xuliangliang
 * @Classname CreateCategoryForm
 * @Description 添加分类
 * @Date 2019/10/21 14:09
 * @blame Java Team
 */
@Data
public class CreateCategoryForm {
    @NotEmpty
    @Max(10)
    private String category;

}
