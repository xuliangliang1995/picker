package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author xuliangliang
 * @Classname RefreshAccessTokenRequest
 * @Description
 * @Date 2019/10/8 21:34
 * @blame Java Team
 */
@Data
public class RefreshAccessTokenRequest extends AbstractRequest {
    @NotEmpty
    private String refreshToken;

    @Override
    public void requestCheck() {

    }


    public static final class Builder {
        private String refreshToken;

        private Builder() {
        }

        public static Builder aRefreshAccessTokenRequest() {
            return new Builder();
        }

        public Builder withRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public RefreshAccessTokenRequest build() {
            RefreshAccessTokenRequest refreshAccessTokenRequest = new RefreshAccessTokenRequest();
            refreshAccessTokenRequest.setRefreshToken(refreshToken);
            return refreshAccessTokenRequest;
        }
    }
}
