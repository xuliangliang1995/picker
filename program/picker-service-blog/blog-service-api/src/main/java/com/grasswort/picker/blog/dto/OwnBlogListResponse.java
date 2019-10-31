package com.grasswort.picker.blog.dto;

import com.grasswort.picker.blog.dto.blog.BlogItem;
import com.grasswort.picker.commons.result.AbstractResponse;
import lombok.Data;

import java.util.List;

/**
 * @author xuliangliang
 * @Classname OwnBlogListResponse
 * @Description 博客列表
 * @Date 2019/10/30 22:13
 * @blame Java Team
 */
@Data
public class OwnBlogListResponse extends AbstractResponse {

    private List<BlogItem> blogs;

    private Long total;
}
