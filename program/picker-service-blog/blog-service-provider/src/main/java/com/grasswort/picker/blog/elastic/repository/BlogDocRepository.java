package com.grasswort.picker.blog.elastic.repository;

import com.grasswort.picker.blog.elastic.entity.BlogDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author xuliangliang
 * @Classname BlogDocRepository
 * @Description 博客存储库
 * @Date 2019/11/30 16:07
 * @blame Java Team
 */
public interface BlogDocRepository extends ElasticsearchRepository<BlogDoc, Long> {


}
