package com.grasswort.picker.commons.encrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author xuliangliang
 * @Classname DESCipher
 * @Description DES 加解密
 * @Date 2019/10/31 17:36
 * @blame Java Team
 */
public class DESCipher implements CipherSymmetric{

    /**
     * 加密
     *
     * @param plainText
     * @param password
     * @return
     */
    @Override
    public String encrypt(String plainText, String password) {
        try {
            // 将密码转为字节数组
            byte[] key = password.getBytes("utf-8");
            // 构造密钥的功能类
            SecretKeySpec skeySpec = new SecretKeySpec(key, "DES");
            // 用指定的加密模式得到一个加密的功能类
            Cipher cipher = Cipher.getInstance("DES");
            // 用密钥和模式初始化功能类
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            // 执行操作
            byte[] decryptText = cipher.doFinal(plainText.getBytes("utf-8"));
            // 转为指定字符串
            return byte2hex(decryptText);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密
     *
     * @param cipherText
     * @param password
     * @return
     */
    @Override
    public String decrypt(String cipherText, String password) {
        try {
            // 将密码转为字节数组
            byte[] key = password.getBytes("utf-8");
            // 构造密钥的功能类
            SecretKeySpec skeySpec = new SecretKeySpec(key, "DES");
            // 用指定的加密模式得到一个加密的功能类
            Cipher cipher = Cipher.getInstance("DES");
            // 用密钥和模式初始化功能类
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            // 执行操作
            byte[] plainText = cipher.doFinal(hex2byte(cipherText));
            // 转为指定字符串
            return new String(plainText, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 16进制字符串转换成字节码
     * @param strhex
     * @return
     */
    private static byte[] hex2byte(String strhex) {
        if (strhex == null) {
            return null;
        }
        int l = strhex.length();
        if (l % 2 == 1) {
            return null;
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / 2; i++) {
            b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2),
                    16);
        }
        return b;
    }

    /**
     * 字节码转换成16进制字符串
     * @param b
     * @return
     */
    private static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }


}
