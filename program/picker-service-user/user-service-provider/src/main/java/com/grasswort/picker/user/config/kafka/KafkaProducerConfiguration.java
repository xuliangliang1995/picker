package com.grasswort.picker.user.config.kafka;

import com.grasswort.picker.user.constants.CAPTCHAEmailMeta;
import com.grasswort.picker.user.constants.PickerActivateEmailMetaData;
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
        return new NewTopic(PickerActivateEmailMetaData.PICKER_ACTIVATE_TOPIC, PickerActivateEmailMetaData.PARTITIONS, PickerActivateEmailMetaData.REPLICATION_FACTOR);
    }

    @Bean
    public NewTopic userCaptchaTopic() {
        return new NewTopic(CAPTCHAEmailMeta.CAPTCHA_EMAIL_TOPIC, CAPTCHAEmailMeta.PATITIONS, CAPTCHAEmailMeta.REPLICATION_FACTOR);
    }
}
