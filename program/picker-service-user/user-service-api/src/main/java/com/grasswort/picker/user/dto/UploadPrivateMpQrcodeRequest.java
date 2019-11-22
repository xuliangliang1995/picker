package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname UploadPrivateMpQrcodeRequest
 * @Description 上传个人公众号二维码
 * @Date 2019/11/22 21:04
 * @blame Java Team
 */
@Data
public class UploadPrivateMpQrcodeRequest extends AbstractRequest {
    @NotNull
    @Min(1)
    private Long userId;
    @NotEmpty
    private String privateMpQrcode;
    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private Long userId;
        private String privateMpQrcode;

        private Builder() {
        }

        public static Builder anUploadPrivateMpQrcodeRequest() {
            return new Builder();
        }

        public Builder withUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder withPrivateMpQrcode(String privateMpQrcode) {
            this.privateMpQrcode = privateMpQrcode;
            return this;
        }

        public UploadPrivateMpQrcodeRequest build() {
            UploadPrivateMpQrcodeRequest uploadPrivateMpQrcodeRequest = new UploadPrivateMpQrcodeRequest();
            uploadPrivateMpQrcodeRequest.setUserId(userId);
            uploadPrivateMpQrcodeRequest.setPrivateMpQrcode(privateMpQrcode);
            return uploadPrivateMpQrcodeRequest;
        }
    }
}
