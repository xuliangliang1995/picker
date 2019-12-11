package com.grasswort.picker.blog.vo;

import lombok.Data;

import javax.validation.constraints.*;

/**
 * @author xuliangliang
 * @Classname TopicMenuForm
 * @Description TopicMenuForm
 * @Date 2019/12/11 15:58
 * @blame Java Team
 */
@Data
public class TopicMenuCreateForm {

    @NotNull
    @Min(0)
    private Long parentMenuId;
    @NotEmpty
    @Size(max = 50)
    private String name;
    @NotNull
    @Min(1)
    @Max(3)
    private Integer type;

    private String blogId;
}
