package com.grasswort.picker.user.util;

import org.junit.Assert;
import org.junit.Test;
import org.msgpack.annotation.Message;

import java.io.IOException;


/**
 * @author xuliangliang
 * @Classname MsgPackUtilTest
 * @Description TODO
 * @Date 2019/10/1 23:12
 * @blame Java Team
 */
public class MsgPackUtilTest {

    @Test
    public void test() {
        final int ID = 1;
        final String NAME = "许良良";
        //MsgPackUtil.register(User.class);
        String text = "";
        try {
            text = MsgPackUtil.write(new User(ID, NAME));
        } catch (IOException e) {
            e.printStackTrace();
            Assert.assertTrue("MsgPack 序列化失败", false);
        }
        User user = null;
        try {
            user = MsgPackUtil.read(text, User.class);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.assertTrue("MsgPack 反序列化失败", false);
        }
        Assert.assertEquals("序列化前后信息不一致", ID, user.id);
        Assert.assertEquals("序列化前后信息不一致", NAME, user.name);
    }

    @Message
    public static class User {
        private int id;
        private String name;

        public User(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public User() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}