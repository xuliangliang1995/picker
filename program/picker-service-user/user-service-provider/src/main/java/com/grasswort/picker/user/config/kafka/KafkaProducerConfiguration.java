package com.grasswort.picker.user.config.kafka;

import com.grasswort.picker.user.constants.PickerActivateMetaData;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xuliangliang
 * @Classname KafkaProducerConfiguration
 * @Description kafka 配置
 * @Date 2019/10/6 21:29
 * @blame Java Team
 */
@Configuration
public class KafkaProducerConfiguration {

    @Bean
    public NewTopic userRegisterTopic() {
        return new NewTopic(PickerActivateMetaData.PICKER_ACTIVATE_TOPIC, PickerActivateMetaData.PATITIONS, PickerActivateMetaData.REPLICATION_FACTOR);
    }
}
