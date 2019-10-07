package com.grasswort.picker.email.serializer;

import com.grasswort.picker.email.model.Mail;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.msgpack.MessagePack;

import java.io.IOException;
import java.util.Map;

/**
 * @author xuliangliang
 * @Classname MailDeserializer
 * @Description Mail 反序列化
 * @Date 2019/10/6 20:11
 * @blame Java Team
 */
@Slf4j
public class MailDeserializer implements Deserializer<Mail> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public Mail deserialize(String s, byte[] bytes) {
        MessagePack messagePack = new MessagePack();
        try {
            Mail mail = messagePack.read(bytes, Mail.class);
            return mail;
        } catch (IOException e) {
            e.printStackTrace();
            log.info("\n邮件反序列化失败：{}", e.getMessage());
        }
        return null;
    }

    @Override
    public void close() {

    }
}
