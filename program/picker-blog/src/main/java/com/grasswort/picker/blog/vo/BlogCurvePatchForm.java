package com.grasswort.picker.blog.vo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author xuliangliang
 * @Classname BlogCurvePatchForm
 * @Description 博客记忆曲线修改表单
 * @Date 2019/11/10 19:39
 * @blame Java Team
 */
@Data
public class BlogCurvePatchForm {
    @Min(0)
    private Integer order;

    @Min(0)
    @Max(1)
    private Integer status;
}
