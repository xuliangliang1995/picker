package com.grasswort.picker.blog.vo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname BlogPoolForm
 * @Description
 * @Date 2019/11/20 16:29
 * @blame Java Team
 */
@Data
public class BlogPoolForm {

    private String keyword;

    private String authorId;
    @NotNull
    @Min(1)
    private Integer pageNo;
    @NotNull
    @Min(1)
    @Max(100)
    private Integer pageSize;
}
