package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import com.grasswort.picker.user.validator.Username;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname QueryActivateStatusRequest
 * @Description 查看激活状态
 * @Date 2019/10/15 16:37
 * @blame Java Team
 */
@Data
public class QueryActivateStatusRequest extends AbstractRequest {
    @NotNull
    @Username
    private String username;

    @Override
    public void requestCheck() {

    }


    public static final class Builder {
        private String username;

        private Builder() {
        }

        public static Builder aQueryActivateStatusRequest() {
            return new Builder();
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public QueryActivateStatusRequest build() {
            QueryActivateStatusRequest queryActivateStatusRequest = new QueryActivateStatusRequest();
            queryActivateStatusRequest.setUsername(username);
            return queryActivateStatusRequest;
        }
    }
}
