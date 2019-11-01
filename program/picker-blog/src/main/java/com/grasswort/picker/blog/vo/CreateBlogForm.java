package com.grasswort.picker.blog.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * @author xuliangliang
 * @Classname CreateBlogForm
 * @Description 创建博客表单
 * @Date 2019/10/21 11:56
 * @blame Java Team
 */
@Data
public class CreateBlogForm {
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
    
    private Set<@Size(min = 1, max = 10) String> labels;
}
