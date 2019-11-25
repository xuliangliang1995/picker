package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author xuliangliang
 * @Classname BlogBrowseRequest
 * @Description 博客浏览记录
 * @Date 2019/11/25 16:56
 * @blame Java Team
 */
@Data
public class BlogBrowseRequest extends AbstractRequest {
    @NotEmpty
    private String blogId;
    @Override
    public void requestCheck() {

    }
}
