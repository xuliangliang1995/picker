package com.grasswort.picker.blog.elastic.repository;

import com.grasswort.picker.blog.elastic.entity.TopicDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author xuliangliang
 * @Classname TopicDocRepository
 * @Description 专题
 * @Date 2019/12/17 11:44
 * @blame Java Team
 */
public interface TopicDocRepository extends ElasticsearchRepository<TopicDoc, Long> {
}
