package com.grasswort.picker.blog.service.elastic;

import com.grasswort.picker.blog.dao.entity.Topic;
import com.grasswort.picker.blog.elastic.repository.TopicDocRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xuliangliang
 * @Classname TopicDocRefreshService
 * @Description
 * @Date 2019/12/17 13:57
 * @blame Java Team
 */
@Service
public class TopicDocRefreshService {
    @Resource
    private TopicDocRepository topicDocRepository;
    @Resource
    private TopicDocConverter topicDocConverter;



    /**
     * 刷新 Topic
     * @param topic
     */
    public void refreshTopic(Topic topic) {
        topicDocRepository.save(topicDocConverter.topic2Doc(topic));
        topicDocRepository.refresh();
    }

}
