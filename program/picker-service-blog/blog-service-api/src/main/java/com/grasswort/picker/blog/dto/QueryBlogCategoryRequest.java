package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xuliangliang
 * @Classname QueryBlogCategoryRequest
 * @Description 查看所有分类
 * @Date 2019/10/21 10:37
 * @blame Java Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryBlogCategoryRequest extends AbstractRequest {

    private Long userId;

    @Override
    public void requestCheck() {

    }
}
