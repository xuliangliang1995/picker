package com.grasswort.picker.user.service;

import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.user.IUserSubscribeService;
import com.grasswort.picker.user.config.kafka.TopicUserDocUpdate;
import com.grasswort.picker.user.constants.DBGroup;
import com.grasswort.picker.user.constants.KafkaTemplateConstant;
import com.grasswort.picker.user.constants.SubscribeBehaviorEnum;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dao.entity.UserSubscribeAuthor;
import com.grasswort.picker.user.dao.entity.UserSubscribeLog;
import com.grasswort.picker.user.dao.persistence.UserSubscribeAuthorMapper;
import com.grasswort.picker.user.dao.persistence.UserSubscribeLogMapper;
import com.grasswort.picker.user.dto.*;
import com.grasswort.picker.user.dto.user.UserItem;
import com.grasswort.picker.user.elastic.entity.UserDoc;
import com.grasswort.picker.user.elastic.repository.UserDocRepository;
import com.grasswort.picker.user.service.elastic.UserDocConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author xuliangliang
 * @Classname UserSubscribeSericeImpl
 * @Description 关注服务
 * @Date 2019/11/21 14:52
 * @blame Java Team
 */
@Slf4j
@Service(version = "1.0", timeout = 10000)
public class UserSubscribeServiceImpl implements IUserSubscribeService {

    @Autowired UserSubscribeAuthorMapper userSubscribeAuthorMapper;

    @Autowired UserSubscribeLogMapper userSubscribeLogMapper;

    @Autowired RedissonClient redissonClient;

    @Autowired UserDocRepository userDocRepository;

    @Autowired UserDocConverter userDocConverter;

    @Autowired @Qualifier(KafkaTemplateConstant.USER_DOC_UPDATE) KafkaTemplate<String, Long> kafkaTemplate;

    // 粉丝列表
    private final String FOLLOWERS_KEY_TEMPLATE = "pk_user:%s:follower";
    // 关注列表
    private final String FOLLOWING_KEY_TEMPLATE = "pk_user:%s:following";

    /**
     * 关注作者
     *
     * @param subscribeRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    @Transactional(rollbackFor = Exception.class)
    public SubscribeResponse subscribe(SubscribeRequest subscribeRequest) {
        SubscribeResponse subscribeResponse = new SubscribeResponse();

        Long pkUserId = subscribeRequest.getUserId();
        Long authorId = subscribeRequest.getAuthorId();

        Example example = new Example(UserSubscribeAuthor.class);
        example.createCriteria().andEqualTo("pkUserId", pkUserId)
                .andEqualTo("authorId", authorId);
        UserSubscribeAuthor author = userSubscribeAuthorMapper.selectOneByExample(example);
        if (author == null) {
            Date now = new Date(System.currentTimeMillis());
            // 订阅
            UserSubscribeAuthor subscribeAuthor = new UserSubscribeAuthor();
            subscribeAuthor.setPkUserId(pkUserId);
            subscribeAuthor.setAuthorId(authorId);
            subscribeAuthor.setGmtCreate(now);
            subscribeAuthor.setGmtModified(now);
            userSubscribeAuthorMapper.insertUseGeneratedKeys(subscribeAuthor);
            // 记录订阅记录
            UserSubscribeLog subscribeLog = new UserSubscribeLog();
            subscribeLog.setPkUserId(pkUserId);
            subscribeLog.setAuthorId(authorId);
            subscribeLog.setBehavior(SubscribeBehaviorEnum.SUBSCRIBE.getId());
            subscribeLog.setGmtCreate(now);
            subscribeLog.setGmtModified(now);
            userSubscribeLogMapper.insertUseGeneratedKeys(subscribeLog);
            // 更新 redis 缓存
            RList<Long> followingList = redissonClient.getList(String.format(FOLLOWING_KEY_TEMPLATE, pkUserId));
            if (followingList.isExists()) {
                followingList.addAsync(authorId);
            }
            RList<Long> followersList = redissonClient.getList(String.format(FOLLOWERS_KEY_TEMPLATE, authorId));
            if (followersList.isExists()) {
                followersList.addAsync(pkUserId);
            }
            // 更新 elastic
            kafkaTemplate.send(TopicUserDocUpdate.TOPIC, pkUserId);
            kafkaTemplate.send(TopicUserDocUpdate.TOPIC, authorId);
        }

        subscribeResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        subscribeResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        return subscribeResponse;
    }

    /**
     * 取消作者关注
     *
     * @param unsubscribeRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    @Transactional(rollbackFor = Exception.class)
    public UnsubscribeResponse unsubscribe(UnsubscribeRequest unsubscribeRequest) {
        UnsubscribeResponse unsubscribeResponse = new UnsubscribeResponse();

        Long pkUserId = unsubscribeRequest.getUserId();
        Long authorId = unsubscribeRequest.getAuthorId();

        Example example = new Example(UserSubscribeAuthor.class);
        example.createCriteria().andEqualTo("pkUserId", pkUserId)
                .andEqualTo("authorId", authorId);
        UserSubscribeAuthor author = userSubscribeAuthorMapper.selectOneByExample(example);
        if (author != null) {
            Date now = new Date(System.currentTimeMillis());
            // 取消订阅
            userSubscribeAuthorMapper.deleteByPrimaryKey(author.getId());
            // 记录取消订阅记录
            UserSubscribeLog subscribeLog = new UserSubscribeLog();
            subscribeLog.setPkUserId(pkUserId);
            subscribeLog.setAuthorId(authorId);
            subscribeLog.setBehavior(SubscribeBehaviorEnum.UNSUBSCRIBE.getId());
            subscribeLog.setGmtCreate(now);
            subscribeLog.setGmtModified(now);
            userSubscribeLogMapper.insertUseGeneratedKeys(subscribeLog);

            // 更新 redis 缓存
            RList<Long> followingList = redissonClient.getList(String.format(FOLLOWING_KEY_TEMPLATE, pkUserId));
            if (followingList.isExists()) {
                followingList.removeAsync(authorId);
            }
            RList<Long> followersList = redissonClient.getList(String.format(FOLLOWERS_KEY_TEMPLATE, authorId));
            if (followersList.isExists()) {
                followersList.removeAsync(pkUserId);
            }

            // 更新 elastic
            kafkaTemplate.send(TopicUserDocUpdate.TOPIC, pkUserId);
            kafkaTemplate.send(TopicUserDocUpdate.TOPIC, authorId);
        }

        unsubscribeResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        unsubscribeResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        return unsubscribeResponse;
    }

    /**
     * 是否关注
     *
     * @param subscribeStatusRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public SubscribeStatusResponse subscribeStatus(SubscribeStatusRequest subscribeStatusRequest) {
        SubscribeStatusResponse subscribeStatusResponse = new SubscribeStatusResponse();

        Long pkUserId = subscribeStatusRequest.getUserId();
        Long authorId = subscribeStatusRequest.getAuthorId();

        subscribeStatusResponse.setSubscribe(userSubscribeAuthorMapper.isSubscribe(pkUserId, authorId));

        subscribeStatusResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        subscribeStatusResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        return subscribeStatusResponse;
    }

    /**
     * 粉丝列表
     *
     * @param followerRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public FollowerResponse followers(FollowerRequest followerRequest) {
        FollowerResponse followerResponse = new FollowerResponse();
        Integer pageNo = followerRequest.getPageNo();
        Integer pageSize = followerRequest.getPageSize();
        Long userId = followerRequest.getUserId();
        Long authorId = followerRequest.getAuthorId();

        RList<Long> followers = redissonClient.getList(String.format(FOLLOWERS_KEY_TEMPLATE, authorId));
        if (! followers.isExists()) {
            followers.addAll(userSubscribeAuthorMapper.followerIdList(authorId));
        }

        long total = followers.size();
        List<UserItem> subFollowers = followers.range(pageSize * (pageNo - 1), pageSize).stream().map(followerId -> {
            Optional<UserDoc> userDoc = userDocRepository.findById(followerId);
            if (userDoc.isPresent()) {
                UserItem userItem = userDocConverter.userDoc2Item(userDoc.get());
                if (userId != null && userId > 0L) {
                    userItem.setSubscribe(userSubscribeAuthorMapper.isSubscribe(userId, authorId));
                }
                return userItem;
            } else {
                // can't reach here
                log.error("未从 ES 中查询到作者信息：{}", followerId);
                followers.deleteAsync();
                return null;
            }
        }).collect(Collectors.toList());

        followerResponse.setFollowers(subFollowers);
        followerResponse.setTotal(total);
        followerResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        followerResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        return followerResponse;
    }

    /**
     * 关注列表
     *
     * @param followingRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public FollowingResponse following(FollowingRequest followingRequest) {
        FollowingResponse followingResponse = new FollowingResponse();
        Integer pageNo = followingRequest.getPageNo();
        Integer pageSize = followingRequest.getPageSize();
        Long userId = followingRequest.getUserId();
        Long authorId = followingRequest.getAuthorId();

        RList<Long> authors = redissonClient.getList(String.format(FOLLOWING_KEY_TEMPLATE, authorId));
        if (! authors.isExists()) {
            authors.addAll(userSubscribeAuthorMapper.followingIdList(authorId));
        }

        long total = authors.size();
        List<UserItem> subFollowingAuthors = authors.range(pageSize * (pageNo - 1), pageSize).stream().map(followerId -> {
            Optional<UserDoc> userDoc = userDocRepository.findById(followerId);
            if (userDoc.isPresent()) {
                UserItem userItem = userDocConverter.userDoc2Item(userDoc.get());
                if (userId != null && userId > 0L) {
                    userItem.setSubscribe(userSubscribeAuthorMapper.isSubscribe(userId, authorId));
                }
                return userItem;
            } else {
                // can't reach here
                log.error("未从 ES 中查询到作者信息：{}", followerId);
                authors.deleteAsync();
                return null;
            }
        }).collect(Collectors.toList());

        followingResponse.setUsers(subFollowingAuthors);
        followingResponse.setTotal(total);
        followingResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        followingResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        return followingResponse;
    }
}
