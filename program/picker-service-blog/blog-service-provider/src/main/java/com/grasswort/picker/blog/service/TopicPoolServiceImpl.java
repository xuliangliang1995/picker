package com.grasswort.picker.blog.service;

import com.grasswort.picker.blog.ITopicPoolService;
import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.constant.TopicStatusEnum;
import com.grasswort.picker.blog.dto.TopicPoolRequest;
import com.grasswort.picker.blog.dto.TopicPoolResponse;
import com.grasswort.picker.blog.service.elastic.TopicDocInitService;
import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.user.util.PickerIdEncrypt;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery("status", TopicStatusEnum.PUBLIC.getStatus()));
        if (authorId != null && authorId > 0L) {
            queryBuilder.filter(QueryBuilders.termQuery("pkUserId", PickerIdEncrypt.encrypt(authorId)));
        }
        if (StringUtils.isNotBlank(keyword)) {
            queryBuilder.must(
                    QueryBuilders.boolQuery()
                            .should(QueryBuilders.prefixQuery("title", keyword))
                            .should(QueryBuilders.prefixQuery("links.title", keyword))
            );
        }

        Sort sort = new Sort(Sort.Direction.DESC, "_score");
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        return null;
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
