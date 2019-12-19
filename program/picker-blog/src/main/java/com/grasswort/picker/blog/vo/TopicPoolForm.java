package com.grasswort.picker.blog.vo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname TopicPoolForm
 * @Description
 * @Date 2019/12/19 14:56
 * @blame Java Team
 */
@Data
public class TopicPoolForm {
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
