package com.grasswort.picker.blog.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * @author xuliangliang
 * @Classname PatchCategoryForm
 * @Description 修改分类
 * @Date 2019/11/6 17:55
 * @blame Java Team
 */
@Data
public class PatchCategoryForm {
    @Min(0)
    private Long parentId;
    @Size(min = 0, max = 20)
    private String category;
}
