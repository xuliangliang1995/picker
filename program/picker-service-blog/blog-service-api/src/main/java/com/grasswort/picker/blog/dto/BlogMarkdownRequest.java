package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author xuliangliang
 * @Classname BlogMarkdownRequest
 * @Description 获取博客 markdown 内容
 * @Date 2019/10/31 18:18
 * @blame Java Team
 */
@Data
public class BlogMarkdownRequest extends AbstractRequest {
    @NotEmpty
    private String blogId;

    @Override
    public void requestCheck() {

    }
}
