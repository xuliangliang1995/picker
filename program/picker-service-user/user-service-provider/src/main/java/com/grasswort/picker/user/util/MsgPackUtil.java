package com.grasswort.picker.user.util;

import org.apache.commons.codec.binary.Base64;
import org.msgpack.MessagePack;

import java.io.IOException;

/**
 * @author xuliangliang
 * @Classname MsgPackUtil
 * @Description TODO
 * @Date 2019/10/1 23:00
 * @blame Java Team
 */
public class MsgPackUtil {

    private static final MessagePack MESSAGE_PACK = new MessagePack();

    /**
     * 序列化
     * @param t
     * @param <T>
     * @return
     */
    public static <T> String write(T t) throws IOException {
        return Base64.encodeBase64String(MESSAGE_PACK.write(t));
    }

    /**
     * 反序列化
     * @param str
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T read(String str, Class<T> v) throws IOException {
        return MESSAGE_PACK.read(Base64.decodeBase64(str), v);
    }

}
