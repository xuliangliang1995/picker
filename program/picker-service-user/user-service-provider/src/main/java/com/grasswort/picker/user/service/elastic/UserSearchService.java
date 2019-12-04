package com.grasswort.picker.user.service.elastic;

import com.grasswort.picker.user.elastic.entity.UserDoc;
import com.grasswort.picker.user.elastic.repository.UserDocRepository;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xuliangliang
 * @Classname UserSearchService
 * @Description 用户查询
 * @Date 2019/12/4 13:56
 * @blame Java Team
 */
@Service
public class UserSearchService {
    @Resource
    private UserDocRepository userDocRepository;

    /**
     * 根据关键字查询
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Page<UserDoc> search(String keyword, Integer pageNo, Integer pageSize) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(keyword)) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("nickName", keyword));
        }
        Sort sort = new Sort(Sort.Direction.DESC, "likedCount");
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<UserDoc> users = userDocRepository.search(boolQueryBuilder, pageable);
        return users;
    }
}
