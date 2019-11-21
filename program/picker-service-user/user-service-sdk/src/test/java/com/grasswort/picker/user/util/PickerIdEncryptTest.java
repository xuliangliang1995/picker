package com.grasswort.picker.user.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author xuliangliang
 * @Classname PickerIdEncryptTest
 * @Date 2019/11/21 10:56
 * @blame Java Team
 */
public class PickerIdEncryptTest {

    @Test
    public void encrypt() {
        long id = Long.MIN_VALUE;
        String text = PickerIdEncrypt.encrypt(id);
        assertEquals("pickerId 加解密不一致", Long.valueOf(id), PickerIdEncrypt.decrypt(text));
    }

    @Test
    public void encrypt2() {
        long id = Long.MAX_VALUE;
        String text = PickerIdEncrypt.encrypt(id);
        assertEquals("pickerId 加解密不一致", Long.valueOf(id), PickerIdEncrypt.decrypt(text));
    }

}