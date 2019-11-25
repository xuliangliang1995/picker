package com.grasswort.picker.blog.dto;

import lombok.Data;

/**
 * @author xuliangliang
 * @Classname BlogLikeStatusResponse
 * @Description 点赞状态
 * @Date 2019/11/25 14:56
 * @blame Java Team
 */
@Data
public class BlogLikeStatusResponse extends AbstractBlogResponse {

    private Boolean like;
}
