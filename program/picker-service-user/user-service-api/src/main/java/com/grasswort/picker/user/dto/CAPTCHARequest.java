package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import com.grasswort.picker.user.constants.CAPTCHAReceiver;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname CAPTCHARequest
 * @Description 验证码请求
 * @Date 2019/10/8 23:05
 * @blame Java Team
 */
@Data
public class CAPTCHARequest extends AbstractRequest {
    @NotNull
    private CAPTCHAReceiver receiver;
    @NotNull
    @Min(1)
    private Long userId;

    @Override
    public void requestCheck() {

    }


    public static final class Builder {
        private CAPTCHAReceiver receiver;
        private Long userId;

        private Builder() {
        }

        public static Builder aCAPTCHARequest() {
            return new Builder();
        }

        public Builder withReceiver(CAPTCHAReceiver receiver) {
            this.receiver = receiver;
            return this;
        }

        public Builder withUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public CAPTCHARequest build() {
            CAPTCHARequest cAPTCHARequest = new CAPTCHARequest();
            cAPTCHARequest.setReceiver(receiver);
            cAPTCHARequest.setUserId(userId);
            return cAPTCHARequest;
        }
    }
}
