package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname PrivateMpQrcodeRequest
 * @Description 获取推广二维码
 * @Date 2019/11/22 21:06
 * @blame Java Team
 */
@Data
public class PrivateMpQrcodeRequest extends AbstractRequest {
    @NotNull
    @Min(1)
    private Long userId;
    @Override
    public void requestCheck() {

    }


    public static final class Builder {
        private Long userId;

        private Builder() {
        }

        public static Builder aPrivateMpQrcodeRequest() {
            return new Builder();
        }

        public Builder withUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public PrivateMpQrcodeRequest build() {
            PrivateMpQrcodeRequest privateMpQrcodeRequest = new PrivateMpQrcodeRequest();
            privateMpQrcodeRequest.setUserId(userId);
            return privateMpQrcodeRequest;
        }
    }
}
