package com.grasswort.picker.blog.service;

import com.grasswort.picker.blog.IBlogTopicService;
import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.constant.TopicStatusEnum;
import com.grasswort.picker.blog.dao.entity.Topic;
import com.grasswort.picker.blog.dao.persistence.TopicMapper;
import com.grasswort.picker.blog.dto.*;
import com.grasswort.picker.blog.dto.topic.TopicItem;
import com.grasswort.picker.blog.elastic.entity.TopicDoc;
import com.grasswort.picker.blog.elastic.repository.TopicDocRepository;
import com.grasswort.picker.blog.service.elastic.TopicDocRefreshService;
import com.grasswort.picker.blog.service.redisson.TopicMenuCacheable;
import com.grasswort.picker.blog.util.TopicIdEncrypt;
import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.user.IUserBaseInfoService;
import com.grasswort.picker.user.util.PickerIdEncrypt;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    @Autowired TopicDocRefreshService topicDocRefreshService;

    @Reference(version = "1.0", timeout = 10000) IUserBaseInfoService iUserBaseInfoService;

    @Autowired TopicDocRepository topicDocRepository;

    @Autowired TopicMenuCacheable topicMenuCacheable;
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
        topicDocRefreshService.refreshTopic(topic);

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
        topicDocRefreshService.refreshTopic(topic);

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

        BoolQueryBuilder queryBuilders = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery("pickerId", PickerIdEncrypt.encrypt(pkUserId)));

        Sort sort = new Sort(Sort.Direction.DESC, "gmtCreate");
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        Page<TopicDoc> page = topicDocRepository.search(queryBuilders, pageable);
        List<TopicItem> topicItems = page.getContent().stream()
                .map(topic -> TopicItem.Builder.aTopicItem()
                        .withTopicId(TopicIdEncrypt.encrypt(topic.getTopicId()))
                        .withPkUserId(topic.getPickerId())
                        .withTitle(topic.getTitle())
                        .withSummary(topic.getSummary())
                        .withCoverImg(topic.getCoverImg())
                        .withOwnerName(topic.getOwnerName())
                        .withOwnerAvatar(topic.getOwnerAvatar())
                        .withStatus(topic.getStatus())
                        .withLinks(topic.getLinks())
                        .withGmtCreate(topic.getGmtCreate())
                        .withGmtModified(topic.getGmtModified())
                        .build())
                .collect(Collectors.toList());

        topicListResponse.setTopics(topicItems);
        topicListResponse.setTotal(page.getTotalElements());
        topicListResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        topicListResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return topicListResponse;

        /// 从 mysal 数据库中查询，暂时不删
        /*Example example = new Example(Topic.class);
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
        }*/
    }

    /**
     * 状态更改
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
        topicDocRefreshService.refreshTopic(topic);

        statusChangeResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        statusChangeResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return statusChangeResponse;
    }

    /**
     * 删除专题
     *
     * @param deleteRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public TopicDeleteResponse deleteTopic(TopicDeleteRequest deleteRequest) {
        TopicDeleteResponse deleteResponse = new TopicDeleteResponse();

        Long topicId = TopicIdEncrypt.decrypt(deleteRequest.getTopicId());
        Long pkUserId = deleteRequest.getPkUserId();
        Topic topic = Optional.ofNullable(topicId)
                .map(topicMapper::selectByPrimaryKey)
                .filter(t -> Objects.equals(t.getPkUserId(), pkUserId))
                .orElse(null);

        if (topic == null) {
            deleteResponse.setCode(SysRetCodeConstants.BLOG_NOT_EXISTS.getCode());
            deleteResponse.setMsg(SysRetCodeConstants.BLOG_NOT_EXISTS.getMsg());
            return deleteResponse;
        }

        topicMapper.deleteByPrimaryKey(topic);
        topicDocRepository.deleteById(topic.getId());
        topicMenuCacheable.removeTopicMenu(topic.getId());

        deleteResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        deleteResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return deleteResponse;
    }


}
