package com.grasswort.picker.blog.service;

import com.grasswort.picker.blog.IBlogRefreshService;
import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.dao.entity.Topic;
import com.grasswort.picker.blog.dao.persistence.TopicMapper;
import com.grasswort.picker.blog.dao.persistence.ext.BlogDao;
import com.grasswort.picker.blog.service.elastic.BlogDocUpdateService;
import com.grasswort.picker.blog.service.elastic.TopicDocRefreshService;
import com.grasswort.picker.commons.annotation.DB;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

/**
 * @author xuliangliang
 * @Classname BlogRefreshService
 * @Description 刷新 ES 存储服务
 * @Date 2019/12/11 11:07
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 10000)
public class BlogRefreshService implements IBlogRefreshService {

    @Autowired BlogDocUpdateService blogDocUpdateService;

    @Autowired BlogDao blogDao;

    @Autowired TopicMapper topicMapper;

    @Autowired TopicDocRefreshService topicDocRefreshService;
    /**
     * 刷新某位作者相关博客 ES 存储
     *
     * @param authorId
     */
    @Override
    @DB(DBGroup.SLAVE)
    public void refreshByAuthorId(Long authorId) {
        if (authorId != null && authorId > 0L) {
            blogDao.listBlogIdByAuthorId(authorId).parallelStream()
                    .forEach(blogId -> blogDocUpdateService.updateBlogDoc(blogId));

            Example example = new Example(Topic.class);
            example.createCriteria().andEqualTo("pkUserId", authorId);
            topicMapper.selectByExample(example).stream()
                    .forEach(topic -> topicDocRefreshService.refreshTopic(topic));
        }
    }
}
