package com.grasswort.picker.blog;

import com.grasswort.picker.blog.dto.*;

/**
 * @author xuliangliang
 * @Classname IBlogLikeService
 * @Description 博客点赞服务
 * @Date 2019/11/25 13:57
 * @blame Java Team
 */
public interface IBlogLikeService {
    /**
     * 点赞状态
     * @param likeStatusRequest
     * @return
     */
    BlogLikeStatusResponse blogLikeStatus(BlogLikeStatusRequest likeStatusRequest);
    /**
     * 点赞
     * @param blogLikeRequest
     * @return
     */
    BlogLikeResponse blogLike(BlogLikeRequest blogLikeRequest);

    /**
     *取消点赞
     * @param blogLikeCancelRequest
     * @return
     */
    BlogLikeCancelResponse blogLikeCancel(BlogLikeCancelRequest blogLikeCancelRequest);

}
