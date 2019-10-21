package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname BlogContentRequest
 * @Description 博客内容
 * @Date 2019/10/19 13:00
 * @blame Java Team
 */
@Data
public class BlogContentRequest extends AbstractRequest {
    /**
     * 博客 id
     */
    @NotNull
    @Min(1)
    private Long blogId;
    /**
     * 浏览者的 id(如果有的话)
     */
    @Min(1)
    private Long browserUserId;

    @Override
    public void requestCheck() {

    }
}
