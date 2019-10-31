package com.grasswort.picker.blog.util;

import com.grasswort.picker.commons.encrypt.CipherSymmetric;
import com.grasswort.picker.commons.encrypt.DESCipher;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author xuliangliang
 * @Classname BlogIdEncrypt
 * @Description 博客 ID 加密
 * @Date 2019/10/31 17:39
 * @blame Java Team
 */
public class BlogIdEncrypt {

    private final static String KEY = "lsjgkldj";

    private final static CipherSymmetric CIPHER = new DESCipher();

    /**
     * 加密 blogId
     * @param blogId
     * @return
     */
    public static String encrypt(long blogId) {
        return CIPHER.encrypt(String.valueOf(blogId), KEY);
    }

    /**
     * 解密
     * @param cipherText
     * @return
     */
    public static Long decrypt(String cipherText) {
        String blogId = CIPHER.decrypt(cipherText, KEY);
        if (NumberUtils.isDigits(blogId)) {
            return Long.valueOf(blogId);
        }
        return null;
    }
}
