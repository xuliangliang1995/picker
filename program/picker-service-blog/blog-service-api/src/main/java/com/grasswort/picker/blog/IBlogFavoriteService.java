package com.grasswort.picker.blog;

import com.grasswort.picker.blog.dto.*;

/**
 * @author xuliangliang
 * @Classname IBlogFavoriteService
 * @Description 收藏
 * @Date 2019/11/25 14:10
 * @blame Java Team
 */
public interface IBlogFavoriteService {

    /**
     * 博客收藏状态
     * @param blogFavoriteStatusRequest
     * @return
     */
    BlogFavoriteStatusResponse blogFavoriteStatus(BlogFavoriteStatusRequest blogFavoriteStatusRequest);

    /**
     * 收藏
     * @param favoriteRequest
     * @return
     */
    BlogFavoriteResponse blogFavorite(BlogFavoriteRequest favoriteRequest);

    /**
     * 取消收藏
     * @param favoriteCancelRequest
     * @return
     */
    BlogFavoriteCancelResponse blogFavoriteCancel(BlogFavoriteCancelRequest favoriteCancelRequest);


}
