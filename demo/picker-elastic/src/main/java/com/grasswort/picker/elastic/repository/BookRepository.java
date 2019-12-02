package com.grasswort.picker.elastic.repository;

import com.grasswort.picker.elastic.entity.BookBean;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author xuliangliang
 * @Classname BookRepository
 * @Description TODO
 * @Date 2019/11/30 9:55
 * @blame Java Team
 */
public interface BookRepository extends ElasticsearchRepository<BookBean, String> {

}
