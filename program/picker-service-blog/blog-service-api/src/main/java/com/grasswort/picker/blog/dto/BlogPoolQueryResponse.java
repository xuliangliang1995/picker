package com.grasswort.picker.blog.dto;

import com.grasswort.picker.blog.dto.blog.BlogItemWithAuthor;
import lombok.Data;

import java.util.List;

/**
 * @author xuliangliang
 * @Classname BlogPoolQueryResponse
 * @Description
 * @Date 2019/11/20 15:50
 * @blame Java Team
 */
@Data
public class BlogPoolQueryResponse extends AbstractBlogResponse {

    private List<BlogItemWithAuthor> blogs;

    private Long total;
}
