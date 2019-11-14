package com.grasswort.picker.wechat.util;

import com.alibaba.fastjson.JSON;
import com.grasswort.picker.commons.encrypt.DESCipher;

/**
 * @author xuliangliang
 * @Classname QrcodeCiperEncrypt
 * @Description 二维码场景加密
 * @Date 2019/11/14 19:51
 * @blame Java Team
 */
public class QrcodeCiperEncrypt extends DESCipher {

    private final String KEY = "hjjkshdg";
    /**
     * 加密
     * @param info
     * @return
     */
    public String encrypt(QrcodeInfo info) {
        String json = JSON.toJSONString(info);
        return encrypt(json, KEY);
    }

    /**
     * 解析
     * @param ciperText
     * @return
     */
    public QrcodeInfo decrypt(String ciperText) {
        String json = decrypt(ciperText, KEY);
        return JSON.parseObject(json, QrcodeInfo.class);
    }

    public final static QrcodeCiperEncrypt INSTANCE = new QrcodeCiperEncrypt();

}
