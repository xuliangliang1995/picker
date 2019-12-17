package com.grasswort.picker.blog.service.elastic;

import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.dao.persistence.TopicMapper;
import com.grasswort.picker.blog.elastic.repository.TopicDocRepository;
import com.grasswort.picker.commons.annotation.DB;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xuliangliang
 * @Classname TopicDocInitService
 * @Description
 * @Date 2019/12/17 13:36
 * @blame Java Team
 */
@Service
public class TopicDocInitService {
    @Resource
    private TopicMapper topicMapper;
    @Resource
    private TopicDocConverter topicDocConverter;
    @Resource
    private TopicDocRepository topicDocRepository;

    /**
     * 初始化
     */
    @DB(DBGroup.SLAVE)
    public void init() {
        topicMapper.selectAll().stream().forEach(topic -> topicDocRepository.save(topicDocConverter.topic2Doc(topic)));
    }
}
