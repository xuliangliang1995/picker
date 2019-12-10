package com.grasswort.picker.blog.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @author xuliangliang
 * @Classname TopicForm
 * @Description 创建专题
 * @Date 2019/12/10 16:13
 * @blame Java Team
 */
@Data
public class TopicForm {
    @NotEmpty
    @Size(max = 50)
    private String title;
    @Size(max = 500)
    private String summary;
    @Size(max = 120)
    private String coverImg;
}
