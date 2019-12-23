package com.grasswort.picker.blog;

import com.grasswort.picker.blog.dto.TopicFavoriteCancelRequest;
import com.grasswort.picker.blog.dto.TopicFavoriteCancelResponse;
import com.grasswort.picker.blog.dto.TopicFavoriteRequest;
import com.grasswort.picker.blog.dto.TopicFavoriteResponse;

/**
 * @author xuliangliang
 * @Classname ITopicFavoriteService
 * @Description
 * @Date 2019/12/20 14:52
 * @blame Java Team
 */
public interface ITopicFavoriteService {

    /**
     * 收藏
     * @param favoriteRequest
     * @return
     */
    TopicFavoriteResponse topicFavorite(TopicFavoriteRequest favoriteRequest);

    /**
     * 取消收藏
     * @param cancelRequest
     * @return
     */
    TopicFavoriteCancelResponse topicFavoriteCancel(TopicFavoriteCancelRequest cancelRequest);
}
