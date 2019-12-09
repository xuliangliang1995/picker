package com.grasswort.picker.blog.service.elastic;

import com.grasswort.picker.blog.configuration.kafka.TopicUpdateBlogDoc;
import com.grasswort.picker.blog.constant.KafkaTemplateConstant;
import com.grasswort.picker.blog.dao.persistence.ext.BlogDao;
import com.grasswort.picker.user.IUserElasticDocUpdateService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xuliangliang
 * @Classname BlogDocUpdateService
 * @Description 博客更新
 * @Date 2019/12/9 14:51
 * @blame Java Team
 */
@Service
public class BlogDocUpdateService {


    @Resource
    BlogDao blogDao;

    @Autowired
    TopicUpdateBlogDoc topicUpdateBlogDoc;

    @Autowired @Qualifier(KafkaTemplateConstant.BLOG_DOC_UPDATE)
    KafkaTemplate<String, Long> kafkaTemplate;

    @Reference(version = "1.0", timeout = 10000)
    IUserElasticDocUpdateService iUserElasticDocUpdateService;

    /**
     * 更新博客 ES 存储
     */
    public void updateBlogDoc(Long blogId) {
        if (blogId == null) {
            return;
        }
        kafkaTemplate.send(topicUpdateBlogDoc.getTopicName(), blogId);
    }

    /**
     * 更新博客以及关联作者 ES 存储
     * @param blogId
     * @param pkUserId
     */
    public void updateBlogDocAndAuthorDoc(Long blogId, Long pkUserId) {
        this.updateBlogDoc(blogId);
        this.updateAuthorDoc(pkUserId);
    }

    /**
     * 更新博客以及关联作者 ES 存储
     * @param blogId
     */
    public void updateBlogDocAndAuthorDoc(Long blogId) {
        this.updateBlogDoc(blogId);
        if (blogId != null) {
            this.updateAuthorDoc(blogDao.getPkUserId(blogId));
        }
    }

    /**
     * 更新作者 ES 存储
     * @param pkUserId
     */
    public void updateAuthorDoc(Long pkUserId) {
        if (pkUserId == null) {
            return;
        }
        iUserElasticDocUpdateService.updateElastic(pkUserId);
    }

    /**
     * 更新作者 ES 存储
     * @param authorIds
     */
    public void updateAuthorDoc(Long... authorIds) {
        if (authorIds != null) {
            for (int i = 0; i < authorIds.length; i++) {
                if (authorIds[i] != null) {
                    this.updateAuthorDoc(authorIds[i]);
                }
            }
        }
    }


}
