package com.grasswort.picker.blog.service;

import com.grasswort.picker.blog.ITopicFavoriteService;
import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.dao.entity.TopicFavorite;
import com.grasswort.picker.blog.dao.persistence.TopicFavoriteMapper;
import com.grasswort.picker.blog.dao.persistence.TopicMapper;
import com.grasswort.picker.blog.dto.TopicFavoriteCancelRequest;
import com.grasswort.picker.blog.dto.TopicFavoriteCancelResponse;
import com.grasswort.picker.blog.dto.TopicFavoriteRequest;
import com.grasswort.picker.blog.dto.TopicFavoriteResponse;
import com.grasswort.picker.blog.util.TopicIdEncrypt;
import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.user.util.PickerIdEncrypt;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author xuliangliang
 * @Classname TopicFavoriteServiceImpl
 * @Description
 * @Date 2019/12/20 15:00
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 10000)
public class TopicFavoriteServiceImpl implements ITopicFavoriteService {

    @Autowired TopicFavoriteMapper topicFavoriteMapper;

    @Autowired TopicMapper topicMapper;

    /**
     * 收藏
     *
     * @param favoriteRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public TopicFavoriteResponse topicFavorite(TopicFavoriteRequest favoriteRequest) {
        TopicFavoriteResponse favoriteResponse = new TopicFavoriteResponse();
        Long userId = favoriteRequest.getUserId();
        Long topicId = TopicIdEncrypt.decrypt(favoriteRequest.getTopicId());

        boolean topicExists = topicId != null && topicMapper.existsWithPrimaryKey(topicId);
        if (! topicExists) {
            favoriteResponse.setCode(SysRetCodeConstants.BLOG_NOT_EXISTS.getCode());
            favoriteResponse.setMsg(SysRetCodeConstants.BLOG_NOT_EXISTS.getMsg());
            return favoriteResponse;
        }

        boolean hasFavorite = topicFavoriteMapper.selectIdByUserIdAndTopicId(userId, topicId) != null;
        if (! hasFavorite) {
            TopicFavorite topicFavorite = new TopicFavorite();
            topicFavorite.setTopicId(topicId);
            topicFavorite.setUserId(userId);
            Date now = new Date();
            topicFavorite.setGmtCreate(now);
            topicFavorite.setGmtModified(now);
            topicFavoriteMapper.insert(topicFavorite);
        }

        favoriteResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        favoriteResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return favoriteResponse;
    }

    /**
     * 取消收藏
     *
     * @param cancelRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public TopicFavoriteCancelResponse topicFavoriteCancel(TopicFavoriteCancelRequest cancelRequest) {
        TopicFavoriteCancelResponse cancelResponse = new TopicFavoriteCancelResponse();
        Long userId = cancelRequest.getUserId();
        Long topicId = PickerIdEncrypt.decrypt(cancelRequest.getTopicId());

        Long id = topicFavoriteMapper.selectIdByUserIdAndTopicId(userId, topicId);
        if (id != null && id > 0L) {
            topicFavoriteMapper.deleteByPrimaryKey(id);
        }

        cancelResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        cancelResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return cancelResponse;
    }
}
