package com.grasswort.picker.blog.service;

import com.grasswort.picker.blog.ITopicPoolService;
import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.constant.TopicStatusEnum;
import com.grasswort.picker.blog.dao.persistence.TopicFavoriteMapper;
import com.grasswort.picker.blog.dto.TopicPoolRequest;
import com.grasswort.picker.blog.dto.TopicPoolResponse;
import com.grasswort.picker.blog.dto.topic.TopicItem;
import com.grasswort.picker.blog.elastic.entity.TopicDoc;
import com.grasswort.picker.blog.elastic.repository.TopicDocRepository;
import com.grasswort.picker.blog.service.elastic.TopicDocConverter;
import com.grasswort.picker.blog.service.elastic.TopicDocInitService;
import com.grasswort.picker.blog.util.TopicIdEncrypt;
import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.user.util.PickerIdEncrypt;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xuliangliang
 * @Classname TopicPoolServiceImpl
 * @Description 专题
 * @Date 2019/12/19 13:54
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 10000)
public class TopicPoolServiceImpl implements ITopicPoolService {

    @Autowired TopicDocInitService topicDocInitService;

    @Autowired TopicDocRepository topicDocRepository;

    @Autowired TopicDocConverter topicDocConverter;

    @Autowired TopicFavoriteMapper topicFavoriteMapper;
    /**
     * 专题池
     *
     * @param poolRequest
     * @return
     */
    @Override
    public TopicPoolResponse topicPool(TopicPoolRequest poolRequest) {
        TopicPoolResponse poolResponse = new TopicPoolResponse();

        String keyword = poolRequest.getKeyword();
        Long authorId = poolRequest.getAuthorId();
        Integer pageNo = poolRequest.getPageNo();
        Integer pageSize = poolRequest.getPageSize();
        Long browseUserId = poolRequest.getBrowseUserId();

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery("status", TopicStatusEnum.PUBLIC.getStatus()));
        if (authorId != null && authorId > 0L) {
            queryBuilder.filter(QueryBuilders.termQuery("pickerId", PickerIdEncrypt.encrypt(authorId)));
        }
        if (StringUtils.isNotBlank(keyword)) {
            queryBuilder.must(
                    QueryBuilders.boolQuery()
                            .should(QueryBuilders.fuzzyQuery("title", keyword))
                            .should(QueryBuilders.fuzzyQuery("links.title", keyword))
            );
        }

        Sort sort = new Sort(Sort.Direction.DESC, "_score");
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        Page<TopicDoc> topicsPage =  topicDocRepository.search(queryBuilder, pageable);
        List<TopicItem> topics = topicsPage.getContent().stream().map(topicDocConverter::topicDoc2Item)
                .collect(Collectors.toList());

        if (browseUserId != null && browseUserId > 0L) {
            topics.stream().forEach(topic -> topic.setFavorite(topicFavoriteMapper.selectIdByUserIdAndTopicId(browseUserId, TopicIdEncrypt.decrypt(topic.getTopicId())) != null));
        }

        poolResponse.setTopics(topics);
        poolResponse.setTotal(topicsPage.getTotalElements());
        poolResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        poolResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        return poolResponse;
    }

    /**
     * 初始化
     */
    @Override
    @DB(DBGroup.MASTER)
    public void init() {
        topicDocInitService.init();
    }
}
