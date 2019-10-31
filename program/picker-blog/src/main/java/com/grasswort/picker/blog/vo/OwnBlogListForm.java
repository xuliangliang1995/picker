package com.grasswort.picker.blog.vo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname OwnBlogListForm
 * @Description 获取自己的博客列表
 * @Date 2019/10/31 9:37
 * @blame Java Team
 */
@Data
public class OwnBlogListForm {

    private Long categoryId;
    @NotNull
    @Min(1)
    private Integer pageNo;
    @NotNull
    @Min(1)
    @Max(100)
    private Integer pageSize;
}
