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

    public static void main(String[] args) {
        System.out.println("2C06F563A1473D849F97D3EFEC7B3936C0C7E383EF6A606C3F7478D869A1ADF43029ECD07E2C3053E3386B54A2831EC25AFF94BE2769DB1EAAF9E163510BEDF1808EA5F0F3864AE5".length());
        /*QrcodeInfo qrcodeInfo = QrcodeCiperEncrypt.INSTANCE.decrypt("2C06F563A1473D849F97D3EFEC7B3936C0C7E383EF6A606C3F7478D869A1ADF43029ECD07E2C3053E3386B54A2831EC25AFF94BE2769DB1EAAF9E163510BEDF1808EA5F0F3864AE5");
        System.out.println(JSON.toJSONString(qrcodeInfo));*/
    }

}
