package com.grasswort.picker.user.elastic.repository;

import com.grasswort.picker.user.elastic.entity.UserDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author xuliangliang
 * @Classname UserDocRepository
 * @Description 用户仓库
 * @Date 2019/12/4 11:54
 * @blame Java Team
 */
public interface UserDocRepository extends ElasticsearchRepository<UserDoc, Long> {
}
