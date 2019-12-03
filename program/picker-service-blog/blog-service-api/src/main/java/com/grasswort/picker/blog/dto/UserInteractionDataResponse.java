package com.grasswort.picker.blog.dto;

import lombok.Data;

/**
 * @author xuliangliang
 * @Classname IUserInteractionDataResponse
 * @Description 用户交互数据查询
 * @Date 2019/12/3 23:36
 * @blame Java Team
 */
@Data
public class UserInteractionDataResponse extends AbstractBlogResponse {
    /**
     * 博客数量
     */
    private Long blogCount;
    /**
     * 获赞数量
     */
    private Long likedCount;
}
