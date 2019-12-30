package com.grasswort.picker.blog.dto;

import com.grasswort.picker.blog.dto.blog.BlogItem;
import com.grasswort.picker.blog.dto.blog.BlogItemWithAuthor;
import lombok.Data;

import java.util.List;

/**
 * @author xuliangliang
 * @Classname BlogFavoriteListResponse.java
 * @Description
 * @Date 2019/12/30
 * @blame Java Team
 */
@Data
public class BlogFavoriteListResponse extends AbstractBlogResponse {

    private List<BlogItemWithAuthor> blogList;

    private Long total;
}
