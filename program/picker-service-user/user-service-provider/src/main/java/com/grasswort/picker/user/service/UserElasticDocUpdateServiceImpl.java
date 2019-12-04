package com.grasswort.picker.user.service;

import com.grasswort.picker.user.IUserElasticDocUpdateService;
import com.grasswort.picker.user.config.kafka.TopicUserDocUpdate;
import com.grasswort.picker.user.constants.KafkaTemplateConstant;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author xuliangliang
 * @Classname UserElasticDocUpdateServiceImpl
 * @Description 用户 es 存储更新
 * @Date 2019/12/4 14:40
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 10000)
public class UserElasticDocUpdateServiceImpl implements IUserElasticDocUpdateService {

    @Autowired @Qualifier(KafkaTemplateConstant.USER_DOC_UPDATE) KafkaTemplate<String, Long> kafkaTemplate;

    @Override
    public void updateElastic(Long pkUserId) {
        if (pkUserId != null && pkUserId > 0) {
            kafkaTemplate.send(TopicUserDocUpdate.TOPIC, pkUserId);
        }
    }
}
