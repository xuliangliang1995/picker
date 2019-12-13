package com.grasswort.picker.blog.vo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname TopicStatusForm
 * @Description
 * @Date 2019/12/13 9:44
 * @blame Java Team
 */
@Data
public class TopicStatusForm {
    @NotNull
    @Min(0)
    @Max(1)
    private Integer status;
}
