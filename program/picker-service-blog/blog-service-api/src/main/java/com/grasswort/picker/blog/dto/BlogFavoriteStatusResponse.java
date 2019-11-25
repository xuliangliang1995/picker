package com.grasswort.picker.blog.dto;

import lombok.Data;

/**
 * @author xuliangliang
 * @Classname BlogFavoriteStatusResponse
 * @Description 收藏状态
 * @Date 2019/11/25 14:58
 * @blame Java Team
 */
@Data
public class BlogFavoriteStatusResponse extends AbstractBlogResponse {

    private Boolean favorite;
}
