package com.grasswort.picker.blog.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * @author xuliangliang
 * @Classname EditBlogForm
 * @Description 编辑博客
 * @Date 2019/11/7 22:51
 * @blame Java Team
 */
@Data
public class EditBlogForm {
    @NotEmpty
    @Size(min = 1, max = 50)
    private String title;
    @NotEmpty
    private String markdown;
    @NotEmpty
    private String html;
    @Min(0)
    private Long categoryId;
    @Size(max = 120)
    private String coverImg;
    @Size(max = 100)
    private String summary;

    private Set<@Size(min = 1, max = 20) String> labels;
}
