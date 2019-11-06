package com.grasswort.picker.blog.vo;

import lombok.Data;

import javax.validation.constraints.*;

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
    @Size(min = 1, max = 20)
    private String category;
    @NotNull
    @Min(0)
    private Long parentId;

}
