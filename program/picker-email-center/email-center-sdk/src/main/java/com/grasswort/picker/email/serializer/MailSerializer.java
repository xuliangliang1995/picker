package com.grasswort.picker.email.serializer;

import com.grasswort.picker.email.model.Mail;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;
import org.msgpack.MessagePack;

import java.io.IOException;
import java.util.Map;

/**
 * @author xuliangliang
 * @Classname MailSerializer
 * @Description 邮件序列化
 * @Date 2019/10/6 20:07
 * @blame Java Team
 */
@Slf4j
public class MailSerializer implements Serializer<Mail> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, Mail mail) {
        MessagePack messagePack = new MessagePack();
        try {
            return messagePack.write(mail);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("\n邮件序列化失败：{}", mail);
        }
        return new byte[0];
    }

    @Override
    public void close() {

    }
}
