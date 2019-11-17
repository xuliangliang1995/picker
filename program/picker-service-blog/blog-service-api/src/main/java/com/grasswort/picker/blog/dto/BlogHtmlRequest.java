package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @author xuliangliang
 * @Classname HtmlRequest
 * @Description 获取博客 HTML
 * @Date 2019/11/16 15:12
 * @blame Java Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogHtmlRequest extends AbstractRequest {
    @NotEmpty
    private String blogId;

    @Override
    public void requestCheck() {

    }
}
