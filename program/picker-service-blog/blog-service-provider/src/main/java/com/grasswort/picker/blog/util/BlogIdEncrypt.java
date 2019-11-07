package com.grasswort.picker.blog.util;

import com.grasswort.picker.commons.encrypt.CipherSymmetric;
import com.grasswort.picker.commons.encrypt.DESCipher;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Optional;

/**
 * @author xuliangliang
 * @Classname BlogIdEncrypt
 * @Description 博客 ID 加密
 * @Date 2019/10/31 17:39
 * @blame Java Team
 */
public class BlogIdEncrypt {

    private final static String KEY = "lsjgkldj";

    private final static String SEPARATOR = "#";

    private final static CipherSymmetric CIPHER = new DESCipher();



    /**
     * 加密 blogId
     * @param blogId
     * @return
     */
    public static String encrypt(long blogId) {
        return Optional.ofNullable(CIPHER.encrypt(String.valueOf(blogId), KEY))
                .map(String::toLowerCase)
                .orElse(null);
    }

    /**
     * 加密 blogId 和 version
     * @param blogId
     * @param version
     * @return
     */
    public static String encrypt(long blogId, int version) {
        String text = blogId + SEPARATOR + version;
        return Optional.ofNullable(CIPHER.encrypt(text, KEY))
                .map(String::toLowerCase)
                .orElse(null);
    }

    /**
     * 解密
     * @param cipherText
     * @return
     */
    public static BlogKey decrypt(String cipherText) {
        if (StringUtils.isNotBlank(cipherText)) {
            cipherText = cipherText.toUpperCase();
        }
        String blogKey = CIPHER.decrypt(cipherText, KEY);
        if (StringUtils.isNotBlank(blogKey)) {
            BlogKey key = new BlogKey();
            if (blogKey.contains(SEPARATOR)) {
                String[] args = blogKey.split(SEPARATOR);
                boolean valid = args.length == 2 && NumberUtils.isDigits(args[0]) && NumberUtils.isDigits(args[1]);
                if (valid) {
                    key.blogId = Long.valueOf(args[0]);
                    key.version = Integer.valueOf(args[1]);
                    return key;
                }
            } else if(NumberUtils.isDigits(blogKey)) {
                key.blogId = Long.valueOf(blogKey);
                key.version = 0;
                return key;
            }
        }
        return null;
    }

    @Data
    public static class BlogKey {

        private long blogId;

        private int version;
    }
}
