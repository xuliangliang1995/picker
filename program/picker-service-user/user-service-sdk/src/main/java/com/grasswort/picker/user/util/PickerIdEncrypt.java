package com.grasswort.picker.user.util;

import com.grasswort.picker.commons.encrypt.CipherSymmetric;
import com.grasswort.picker.commons.encrypt.DESCipher;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * @author xuliangliang
 * @Classname BlogIdEncrypt
 * @Description Picker ID 加解密
 * @Date 2019/10/31 17:39
 * @blame Java Team
 */
public class PickerIdEncrypt {

    private final static String KEY = "slkdjkgk";

    private final static CipherSymmetric CIPHER = new DESCipher();

    /**
     * 加密 blogId
     * @param userId
     * @return
     */
    public static String encrypt(long userId) {
        return Optional.ofNullable(CIPHER.encrypt(String.valueOf(userId), KEY))
                .map(String::toLowerCase)
                .orElse(null);
    }

    /**
     * 解密
     * @param cipherText
     * @return
     */
    public static Long decrypt(String cipherText) {
        if (StringUtils.isNotBlank(cipherText)) {
            cipherText = cipherText.toUpperCase();
        }
        String blogKey = CIPHER.decrypt(cipherText, KEY);
        return Long.valueOf(blogKey);
    }


}
