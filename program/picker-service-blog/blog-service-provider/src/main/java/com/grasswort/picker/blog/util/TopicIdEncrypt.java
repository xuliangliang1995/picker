package com.grasswort.picker.blog.util;

import com.grasswort.picker.commons.encrypt.CipherSymmetric;
import com.grasswort.picker.commons.encrypt.DESCipher;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Optional;

/**
 * @author xuliangliang
 * @Classname TopicIdEncrypt
 * @Description 专题id
 * @Date 2019/12/10 16:01
 * @blame Java Team
 */
public class TopicIdEncrypt {
    private final static String KEY = "lsjgldgk";

    private final static CipherSymmetric CIPHER = new DESCipher();

    /**
     * 加密 topicId
     * @param topicId
     * @return
     */
    public static String encrypt(long topicId) {
        return Optional.ofNullable(CIPHER.encrypt(String.valueOf(topicId), KEY))
                .map(String::toLowerCase)
                .orElse(null);
    }

    /**
     * 解密专题ID
     * @param cipherText
     * @return
     */
    public static Long decrypt(String cipherText) {
        if (StringUtils.isBlank(cipherText)) {
            return null;
        }
        String topicId = CIPHER.decrypt(cipherText, KEY);
        if (NumberUtils.isDigits(topicId)) {
            return Long.valueOf(topicId);
        }
        return null;
    }

}
