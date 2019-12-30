package com.grasswort.picker.blog.service;

import com.grasswort.picker.blog.ITopicFavoriteService;
import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.dao.entity.TopicFavorite;
import com.grasswort.picker.blog.dao.persistence.TopicFavoriteMapper;
import com.grasswort.picker.blog.dao.persistence.TopicMapper;
import com.grasswort.picker.blog.dto.*;
import com.grasswort.picker.blog.dto.topic.TopicItem;
import com.grasswort.picker.blog.elastic.entity.TopicDoc;
import com.grasswort.picker.blog.elastic.repository.TopicDocRepository;
import com.grasswort.picker.blog.service.elastic.TopicDocConverter;
import com.grasswort.picker.blog.service.redisson.TopicFavoriteCacheable;
import com.grasswort.picker.blog.util.TopicIdEncrypt;
import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.user.util.PickerIdEncrypt;
import org.apache.dubbo.config.annotation.Service;
import org.redisson.api.RList;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired TopicDocRepository topicDocRepository;

    @Autowired TopicFavoriteCacheable topicFavoriteCacheable;

    @Autowired TopicDocConverter topicDocConverter;

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
            topicFavoriteCacheable.clear(userId);
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
            topicFavoriteCacheable.clear(userId);
        }

        cancelResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        cancelResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return cancelResponse;
    }

    /**
     * 收藏列表
     *
     * @param favoriteListRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public TopicFavoriteListResponse listTopicFavorite(TopicFavoriteListRequest favoriteListRequest) {
        TopicFavoriteListResponse favoriteListResponse = new TopicFavoriteListResponse();
        Long authorId = favoriteListRequest.getAuthorId();
        Long browseId = favoriteListRequest.getPickerId();
        Integer pageNo = favoriteListRequest.getPageNo();
        Integer pageSize = favoriteListRequest.getPageSize();

        RList<Long> rTopicFavoriteList = topicFavoriteCacheable.listFavorite(authorId);
        if (rTopicFavoriteList == null) {
            List<Long> topicIds = topicFavoriteMapper.listTopicFavorite(authorId);
            topicFavoriteCacheable.cacheUserFavorite(authorId, topicIds);
            rTopicFavoriteList = topicFavoriteCacheable.listFavorite(authorId);
        }
        if (rTopicFavoriteList != null) {
            List<Long> topicIds = rTopicFavoriteList.range(pageSize * (pageNo - 1), pageNo * pageSize - 1);

            favoriteListResponse.setTotal(Long.valueOf(rTopicFavoriteList.size()));
            favoriteListResponse.setTopics(topicIds.stream().map(topicId -> {
                Optional<TopicDoc> topicDocOpt = topicDocRepository.findById(topicId);
                if (topicDocOpt.isPresent()) {
                    TopicItem item = topicDocConverter.topicDoc2Item(topicDocOpt.get());
                    if (browseId != null) {
                        item.setFavorite(topicFavoriteMapper.selectIdByUserIdAndTopicId(browseId, topicId) != null);
                    } else {
                        item.setFavorite(false);
                    }
                    return item;
                }
                return null;
            }).filter(i -> i != null).collect(Collectors.toList()));
        } else {
            favoriteListResponse.setTopics(Collections.EMPTY_LIST);
            favoriteListResponse.setTotal(0L);
        }

        favoriteListResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        favoriteListResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return favoriteListResponse;
    }

}
