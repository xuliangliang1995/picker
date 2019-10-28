package com.grasswort.picker.blog.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

/**
 * @author xuliangliang
 * @Classname CreateBlogForm
 * @Description 创建博客表单
 * @Date 2019/10/21 11:56
 * @blame Java Team
 */
@Data
public class CreateBlogForm {
    @NotEmpty
    private String markdown;
    @NotEmpty
    private String html;
    @Min(1)
    private Long categoryId;
}
