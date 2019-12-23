package com.grasswort.picker.blog.vo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname TopicCommentsForm
 * @Description
 * @Date 2019/12/23 17:12
 * @blame Java Team
 */
@Data
public class TopicCommentsForm {
    @Min(1)
    @NotNull
    private Integer pageNo;
    @NotNull
    @Min(1)
    @Max(100)
    private Integer pageSize;
}
