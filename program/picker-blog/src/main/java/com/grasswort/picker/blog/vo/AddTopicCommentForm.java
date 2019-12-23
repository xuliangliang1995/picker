package com.grasswort.picker.blog.vo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author xuliangliang
 * @Classname AddTopicCommentForm
 * @Description
 * @Date 2019/12/23 15:53
 * @blame Java Team
 */
@Data
public class AddTopicCommentForm {

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rate;

    @Size(max = 500)
    private String content;

}
