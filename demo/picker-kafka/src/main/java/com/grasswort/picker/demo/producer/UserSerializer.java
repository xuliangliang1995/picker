package com.grasswort.picker.demo.producer;

import org.apache.kafka.common.serialization.Serializer;
import org.msgpack.MessagePack;

import java.io.IOException;
import java.util.Map;

/**
 * @author xuliangliang
 * @Classname UserSerializer
 * @Description TODO
 * @Date 2019/10/3 19:07
 * @blame Java Team
 */
public class UserSerializer implements Serializer<User> {

    @Override
    public void configure(Map<String, ?> map, boolean b) {
        // 不做任何配置
    }

    @Override
    public byte[] serialize(String s, User user) {
        MessagePack messagePack = new MessagePack();
        try {
            return messagePack.write(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() {
        // 不需要关闭任何东西
    }
}
