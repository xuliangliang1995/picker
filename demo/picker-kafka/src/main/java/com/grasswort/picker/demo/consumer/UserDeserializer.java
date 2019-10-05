package com.grasswort.picker.demo.consumer;

import com.grasswort.picker.demo.producer.User;
import org.apache.kafka.common.serialization.Deserializer;
import org.msgpack.MessagePack;

import java.io.IOException;
import java.util.Map;

/**
 * @author xuliangliang
 * @Classname UserDeserializer
 * @Description User 反序列化
 * @Date 2019/10/4 15:54
 * @blame Java Team
 */
public class UserDeserializer implements Deserializer<User> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public User deserialize(String topic, byte[] bytes) {
        MessagePack messagePack = new MessagePack();
        try {
            User user = messagePack.read(bytes, User.class);
            return user;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() {

    }
}
