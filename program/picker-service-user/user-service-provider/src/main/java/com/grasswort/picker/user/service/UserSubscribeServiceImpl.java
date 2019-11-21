package com.grasswort.picker.user.service;

import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.user.IUserSubscribeService;
import com.grasswort.picker.user.constants.DBGroup;
import com.grasswort.picker.user.constants.SubscribeBehaviorEnum;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dao.entity.UserSubscribeAuthor;
import com.grasswort.picker.user.dao.entity.UserSubscribeLog;
import com.grasswort.picker.user.dao.persistence.UserSubscribeAuthorMapper;
import com.grasswort.picker.user.dao.persistence.UserSubscribeLogMapper;
import com.grasswort.picker.user.dto.*;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

/**
 * @author xuliangliang
 * @Classname UserSubscribeSericeImpl
 * @Description 关注服务
 * @Date 2019/11/21 14:52
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 10000)
public class UserSubscribeServiceImpl implements IUserSubscribeService {

    @Autowired UserSubscribeAuthorMapper userSubscribeAuthorMapper;

    @Autowired UserSubscribeLogMapper userSubscribeLogMapper;
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

        Example example = new Example(UserSubscribeAuthor.class);
        example.createCriteria().andEqualTo("pkUserId", pkUserId)
                .andEqualTo("authorId", authorId);
        UserSubscribeAuthor author = userSubscribeAuthorMapper.selectOneByExample(example);
        subscribeStatusResponse.setSubscribe(author != null);

        subscribeStatusResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        subscribeStatusResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        return subscribeStatusResponse;
    }
}
