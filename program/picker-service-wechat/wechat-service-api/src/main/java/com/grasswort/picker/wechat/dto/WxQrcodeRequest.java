package com.grasswort.picker.wechat.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author xuliangliang
 * @Classname WxQrcodeRequest
 * @Description 二维码生成请求
 * @Date 2019/11/14 19:19
 * @blame Java Team
 */
@Data
public class WxQrcodeRequest extends AbstractRequest {
    /**
     * 场景
     */
    @NotEmpty
    private String scene;
    @Max(2592000)
    @Min(-1)
    private Integer expireSeconds = -1;
    /**
     * 场景其它信息
     */
    private String text;
    /**
     * 回调地址
     */
    private String callback;

    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private String scene;
        private Integer expireSeconds = -1;
        private String text;
        private String callback;

        private Builder() {
        }

        public static Builder aWxQrcodeRequest() {
            return new Builder();
        }

        public Builder withScene(String scene) {
            this.scene = scene;
            return this;
        }

        public Builder withExpireSeconds(Integer expireSeconds) {
            this.expireSeconds = expireSeconds;
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

        public WxQrcodeRequest build() {
            WxQrcodeRequest wxQrcodeRequest = new WxQrcodeRequest();
            wxQrcodeRequest.setScene(scene);
            wxQrcodeRequest.setExpireSeconds(expireSeconds);
            wxQrcodeRequest.setText(text);
            wxQrcodeRequest.setCallback(callback);
            return wxQrcodeRequest;
        }
    }
}
