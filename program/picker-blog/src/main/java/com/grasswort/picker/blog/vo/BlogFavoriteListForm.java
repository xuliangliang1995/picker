package com.grasswort.picker.blog.vo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname BlogFavoriteListForm.java
 * @Description
 * @Date 2019/12/30
 * @blame Java Team
 */
@Data
public class BlogFavoriteListForm {
    @Min(1)
    @NotNull
    private Integer pageNo;
    @Min(1)
    @Max(100)
    @NotNull
    private Integer pageSize;
    @NotEmpty
    private String authorId;
}
