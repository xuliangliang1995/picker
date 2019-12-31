package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author xuliangliang
 * @Classname EditGithubUrlRequest.java
 * @Description
 * @Date 2019/12/31
 * @blame Java Team
 */
@Data
public class EditGithubUrlRequest extends AbstractRequest {
    @NotNull
    @Min(1)
    private Long userId;
    @NotEmpty
    @Size(max = 45)
    private String github;

    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private Long userId;
        private String github;

        private Builder() {
        }

        public static Builder anEditGithubUrlRequest() {
            return new Builder();
        }

        public Builder withUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder withGithub(String github) {
            this.github = github;
            return this;
        }

        public EditGithubUrlRequest build() {
            EditGithubUrlRequest editGithubUrlRequest = new EditGithubUrlRequest();
            editGithubUrlRequest.setUserId(userId);
            editGithubUrlRequest.setGithub(github);
            return editGithubUrlRequest;
        }
    }
}
