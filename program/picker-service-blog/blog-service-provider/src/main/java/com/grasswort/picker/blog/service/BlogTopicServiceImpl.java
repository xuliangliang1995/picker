package com.grasswort.picker.blog.service;

import com.grasswort.picker.blog.IBlogTopicService;
import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.dao.entity.Topic;
import com.grasswort.picker.blog.dao.persistence.TopicMapper;
import com.grasswort.picker.blog.dto.MyTopicListRequest;
import com.grasswort.picker.blog.dto.MyTopicListResponse;
import com.grasswort.picker.blog.dto.TopicCreateRequest;
import com.grasswort.picker.blog.dto.TopicCreateResponse;
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

import java.util.Collections;
import java.util.Date;
import java.util.List;
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
}
