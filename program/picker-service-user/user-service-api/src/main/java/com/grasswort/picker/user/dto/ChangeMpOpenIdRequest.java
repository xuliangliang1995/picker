package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import com.grasswort.picker.commons.result.AbstractResponse;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author xuliangliang
 * @Classname MpOpenIdRequest
 * @Description 修改 openId 请求
 * @Date 2019/11/15 14:10
 * @blame Java Team
 */
@Data
public class ChangeMpOpenIdRequest extends AbstractRequest {
    @NotNull
    @Min(1)
    private Long userId;
    @NotNull
    @Size(min = 28, max = 28)
    private String openId;

    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private Long userId;
        private String openId;

        private Builder() {
        }

        public static Builder aChangeMpOpenIdRequest() {
            return new Builder();
        }

        public Builder withUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder withOpenId(String openId) {
            this.openId = openId;
            return this;
        }

        public ChangeMpOpenIdRequest build() {
            ChangeMpOpenIdRequest changeMpOpenIdRequest = new ChangeMpOpenIdRequest();
            changeMpOpenIdRequest.setUserId(userId);
            changeMpOpenIdRequest.setOpenId(openId);
            return changeMpOpenIdRequest;
        }
    }
}
