package com.grasswort.picker.wechat.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author xuliangliang
 * @Classname WxMpAuthRequest
 * @Description 微信公众号回调地址认证
 * @Date 2019/11/14 15:00
 * @blame Java Team
 */
@Data
public class WxMpAuthRequest extends AbstractRequest {
    @NotEmpty
    String signature;
    @NotEmpty
    String timestamp;
    @NotEmpty
    String nonce;
    @NotEmpty
    String echostr;

    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        String signature;
        String timestamp;
        String nonce;
        String echostr;

        private Builder() {
        }

        public static Builder aWxMpAuthRequest() {
            return new Builder();
        }

        public Builder withSignature(String signature) {
            this.signature = signature;
            return this;
        }

        public Builder withTimestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder withNonce(String nonce) {
            this.nonce = nonce;
            return this;
        }

        public Builder withEchostr(String echostr) {
            this.echostr = echostr;
            return this;
        }

        public WxMpAuthRequest build() {
            WxMpAuthRequest wxMpAuthRequest = new WxMpAuthRequest();
            wxMpAuthRequest.setSignature(signature);
            wxMpAuthRequest.setTimestamp(timestamp);
            wxMpAuthRequest.setNonce(nonce);
            wxMpAuthRequest.setEchostr(echostr);
            return wxMpAuthRequest;
        }
    }
}
