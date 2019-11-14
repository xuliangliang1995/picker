package com.grasswort.picker.wechat.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname WxMpCallbackRequest
 * @Description 微信回调请求
 * @Date 2019/11/14 15:03
 * @blame Java Team
 */
@Data
public class WxMpCallbackRequest extends AbstractRequest {

    String requestBody;

    String signature;

    String encType;

    String msgSignature;

    String timestamp;

    String nonce;

    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        String requestBody;
        String signature;
        String encType;
        String msgSignature;
        String timestamp;
        String nonce;

        private Builder() {
        }

        public static Builder aWxMpCallbackRequest() {
            return new Builder();
        }

        public Builder withRequestBody(String requestBody) {
            this.requestBody = requestBody;
            return this;
        }

        public Builder withSignature(String signature) {
            this.signature = signature;
            return this;
        }

        public Builder withEncType(String encType) {
            this.encType = encType;
            return this;
        }

        public Builder withMsgSignature(String msgSignature) {
            this.msgSignature = msgSignature;
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

        public WxMpCallbackRequest build() {
            WxMpCallbackRequest wxMpCallbackRequest = new WxMpCallbackRequest();
            wxMpCallbackRequest.setRequestBody(requestBody);
            wxMpCallbackRequest.setSignature(signature);
            wxMpCallbackRequest.setEncType(encType);
            wxMpCallbackRequest.setMsgSignature(msgSignature);
            wxMpCallbackRequest.setTimestamp(timestamp);
            wxMpCallbackRequest.setNonce(nonce);
            return wxMpCallbackRequest;
        }
    }
}
