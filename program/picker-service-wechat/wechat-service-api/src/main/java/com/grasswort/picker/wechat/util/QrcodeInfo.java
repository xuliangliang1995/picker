package com.grasswort.picker.wechat.util;

import lombok.Data;

/**
 * @author xuliangliang
 * @Classname QrcodeInfo
 * @Description 二维码携带信息
 * @Date 2019/11/14 20:01
 * @blame Java Team
 */
@Data
public class QrcodeInfo {
    /**
     * 场景值
     */
    private String scene;
    /**
     * 场景
     */
    private String text;
    /**
     * 回调地址
     */
    private String callback;


    public static final class Builder {
        private String scene;
        private String text;
        private String callback;

        private Builder() {
        }

        public static Builder aQrcodeInfo() {
            return new Builder();
        }

        public Builder withScene(String scene) {
            this.scene = scene;
            return this;
        }

        public Builder withText(String text) {
            this.text = text;
            return this;
        }

        public Builder withCallback(String callback) {
            this.callback = callback;
            return this;
        }

        public QrcodeInfo build() {
            QrcodeInfo qrcodeInfo = new QrcodeInfo();
            qrcodeInfo.setScene(scene);
            qrcodeInfo.setText(text);
            qrcodeInfo.setCallback(callback);
            return qrcodeInfo;
        }
    }
}
