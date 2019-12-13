package com.grasswort.picker.blog.service;

import com.grasswort.picker.blog.IBlogTopicService;
import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.constant.TopicStatusEnum;
import com.grasswort.picker.blog.dao.entity.Topic;
import com.grasswort.picker.blog.dao.persistence.TopicMapper;
import com.grasswort.picker.blog.dto.*;
import com.grasswort.picker.blog.dto.topic.TopicItem;
import com.grasswort.picker.blog.util.TopicIdEncrypt;
import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.user.IUserBaseInfoService;
import com.grasswort.picker.user.dto.UserBaseInfoRequest;
import com.grasswort.picker.user.dto.UserBaseInfoResponse;
import com.grasswort.picker.user.util.PickerIdEncrypt;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xuliangliang
 * @Classname BlogTopicServiceImpl
 * @Description
 * @Date 2019/12/10 15:35
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 10000)
public class BlogTopicServiceImpl implements IBlogTopicService {

    @Autowired TopicMapper topicMapper;

    @Reference(version = "1.0", timeout = 10000) IUserBaseInfoService iUserBaseInfoService;
    /**
     * 创建专题
     *
     * @param createRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public TopicCreateResponse createTopic(TopicCreateRequest createRequest) {
        TopicCreateResponse createResponse = new TopicCreateResponse();

        Topic topic = new Topic();
        topic.setPkUserId(createRequest.getPkUserId());
        topic.setTitle(createRequest.getTitle());
        topic.setStatus(TopicStatusEnum.PRIVATE.getStatus());
        topic.setSummary(createRequest.getSummary());
        topic.setCoverImg(createRequest.getCoverImg());
        Date now = new Date();
        topic.setGmtCreate(now);
        topic.setGmtModified(now);
        topicMapper.insertUseGeneratedKeys(topic);

        createResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        createResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return createResponse;
    }

    /**
     * 编辑专题
     *
     * @param editRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public TopicEditResponse editTopic(TopicEditRequest editRequest) {
        TopicEditResponse editResponse = new TopicEditResponse();

        Long topicId = TopicIdEncrypt.decrypt(editRequest.getTopicId());
        Long pkUserId = editRequest.getPkUserId();
        Topic topic = Optional.ofNullable(topicId)
                .map(topicMapper::selectByPrimaryKey)
                .filter(t -> Objects.equals(t.getPkUserId(), pkUserId))
                .orElse(null);

        if (topic == null) {
            editResponse.setCode(SysRetCodeConstants.BLOG_NOT_EXISTS.getCode());
            editResponse.setMsg(SysRetCodeConstants.BLOG_NOT_EXISTS.getMsg());
            return editResponse;
        }
        topic.setTitle(editRequest.getTitle());
        topic.setSummary(editRequest.getSummary());
        topic.setCoverImg(editRequest.getCoverImg());
        topic.setGmtModified(new Date());
        topicMapper.updateByPrimaryKey(topic);

        editResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        editResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return editResponse;
    }

    /**
     * 专题列表
     *
     * @param topicListRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public MyTopicListResponse topics(MyTopicListRequest topicListRequest) {
        MyTopicListResponse topicListResponse = new MyTopicListResponse();
        Long pkUserId = topicListRequest.getPkUserId();
        Integer pageNo = topicListRequest.getPageNo();
        Integer pageSize = topicListRequest.getPageSize();

        Example example = new Example(Topic.class);
        example.createCriteria().andEqualTo("pkUserId", pkUserId);
        long total = topicMapper.selectCountByExample(example);

        if (total > 0L) {
            UserBaseInfoResponse baseInfoResponse = iUserBaseInfoService.userBaseInfo(
                    UserBaseInfoRequest.Builder.anUserBaseInfoRequest()
                            .withUserId(pkUserId)
                            .build()
            );
            String ownerName = baseInfoResponse.getName();
            String avatar = baseInfoResponse.getAvatar();

            RowBounds rowBounds = new RowBounds(pageSize * (pageNo - 1), pageSize);
            example.setOrderByClause("id desc");

            List<Topic> topics = topicMapper.selectByExampleAndRowBounds(example, rowBounds);
            List<TopicItem> topicItems = topics.stream()
                    .map(topic -> TopicItem.Builder.aTopicItem()
                            .withTopicId(TopicIdEncrypt.encrypt(topic.getId()))
                            .withPkUserId(PickerIdEncrypt.encrypt(topic.getPkUserId()))
                            .withTitle(topic.getTitle())
                            .withSummary(topic.getSummary())
                            .withCoverImg(topic.getCoverImg())
                            .withOwnerName(ownerName)
                            .withOwnerAvatar(avatar)
                            .withStatus(topic.getStatus())
                            .withGmtCreate(topic.getGmtCreate())
                            .withGmtModified(topic.getGmtModified())
                            .build())
                    .collect(Collectors.toList());
            topicListResponse.setTotal(total);
            topicListResponse.setTopics(topicItems);
        } else {
            topicListResponse.setTotal(total);
            topicListResponse.setTopics(Collections.EMPTY_LIST);
        }
        topicListResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        topicListResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return topicListResponse;
    }

    /**
     * 博客状态更改
     *
     * @param changeRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public TopicStatusChangeResponse changeStatus(TopicStatusChangeRequest changeRequest) {
        TopicStatusChangeResponse statusChangeResponse = new TopicStatusChangeResponse();


        Long topicId = TopicIdEncrypt.decrypt(changeRequest.getTopicId());
        Long pkUserId = changeRequest.getPkUserId();
        Topic topic = Optional.ofNullable(topicId)
                .map(topicMapper::selectByPrimaryKey)
                .filter(t -> Objects.equals(t.getPkUserId(), pkUserId))
                .orElse(null);

        if (topic == null) {
            statusChangeResponse.setCode(SysRetCodeConstants.BLOG_NOT_EXISTS.getCode());
            statusChangeResponse.setMsg(SysRetCodeConstants.BLOG_NOT_EXISTS.getMsg());
            return statusChangeResponse;
        }

        Integer status = changeRequest.getStatus();
        Topic topicSelective = new Topic();
        topicSelective.setId(topicId);
        topicSelective.setStatus(status);
        topicSelective.setGmtModified(new Date());
        topicMapper.updateByPrimaryKeySelective(topicSelective);

        statusChangeResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        statusChangeResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return statusChangeResponse;
    }
}
