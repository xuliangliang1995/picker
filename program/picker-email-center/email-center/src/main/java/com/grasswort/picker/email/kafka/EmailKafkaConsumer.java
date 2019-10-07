package com.grasswort.picker.email.kafka;

import com.grasswort.picker.commons.email.DefaultEmailSender;
import com.grasswort.picker.commons.email.MailBody;
import com.grasswort.picker.email.constants.EmailCenterConstant;
import com.grasswort.picker.email.model.Mail;
import com.grasswort.picker.email.serializer.MailDeserializer;
import com.grasswort.picker.email.threadpool.EmailConsumerThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author xuliangliang
 * @Classname EmailConsumer
 * @Description 邮件消费者
 * @Date 2019/10/6 20:58
 * @blame Java Team
 */
@Slf4j
@Configuration
public class EmailKafkaConsumer {
    @Autowired
    KafkaProperties kafkaProperties;

    @Autowired
    DefaultEmailSender defaultEmailSender;

    @Bean
    public KafkaEmailConsumerFactory kafkaEmailConsumerFactory() {
        Map<String, Object> consumerProps = kafkaProperties.buildConsumerProperties();
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getCanonicalName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MailDeserializer.class.getCanonicalName());
        return new KafkaEmailConsumerFactory(consumerProps);
    }

    /**
     * 消费者监听工厂
     * @return
     */
    @Bean
    public KafkaListenerContainerFactory userRegisterSuccKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory conFactory = new ConcurrentKafkaListenerContainerFactory<>();
        conFactory.setConsumerFactory(kafkaEmailConsumerFactory());
        //  设置消费者消费消息后的提交方式为手动提交
        conFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return conFactory;
    }

    @KafkaListener(topicPattern = EmailCenterConstant.LISTENING_TOPIC, containerFactory = "userRegisterSuccKafkaListenerContainerFactory")
    public void consumer(ConsumerRecord<String, Mail> record, Acknowledgment acknowledgment) {
        log.info("\n主题：{}\n分区：{}\n偏移量：{}", record.topic(), record.partition(), record.offset());

        Mail mail = record.value();
        // 交给线程池去执行
        EmailConsumerThreadPool.execute(() -> {
            try {
                doSendMail(mail);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("\n邮件发送异常：{}", mail.getContent());
            }
        });

        acknowledgment.acknowledge();
    }

    /**
     * 发送邮件
     * @param mail
     */
    private void doSendMail(Mail mail) throws Exception {
        MailBody mailBody = new MailBody();
        mailBody.setSubject(mail.getSubject());
        mailBody.setContent(mail.getContent());
        mailBody.setToAddress(mail.getToAddress());
        mailBody.setCcAddress(mail.getCcAddress());
        if (mail.isHtml()) {
            mailBody.setContentType(EmailCenterConstant.CONTENT_TYPE);
            defaultEmailSender.sendHtmlMail(mailBody);
        } else {
            defaultEmailSender.sendMail(mailBody);
        }
    }
}
