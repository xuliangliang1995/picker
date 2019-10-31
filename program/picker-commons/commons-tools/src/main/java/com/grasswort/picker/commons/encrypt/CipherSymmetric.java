package com.grasswort.picker.commons.encrypt;

/**
 * @author xuliangliang
 * @Classname CipherSymmetric
 * @Description 对称加密
 * @Date 2019/10/31 17:43
 * @blame Java Team
 */
public interface CipherSymmetric {

    /**
     * 加密
     * @param plainText
     * @param password
     * @return
     */
    String encrypt(String plainText, String password);

    /**
     * 解密
     * @param cipherText
     * @param password
     * @return
     */
    String decrypt(String cipherText, String password);
}
