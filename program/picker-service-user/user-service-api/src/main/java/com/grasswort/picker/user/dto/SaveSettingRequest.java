package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author xuliangliang
 * @Classname SaveSettingRequest
 * @Description 保存设置
 * @Date 2019/11/4 15:48
 * @blame Java Team
 */
@Data
public class SaveSettingRequest extends AbstractRequest {
    @NotNull
    @Min(1)
    private Long userId;
    @NotEmpty
    @Size(min = 0, max = 20)
    private String markdownTheme;
    @NotNull
    @Min(0)
    private Integer safetyCheckMode;
    @NotNull
    private Boolean openBlogPush;
    @NotNull
    @Min(0)
    private Integer blogPushMode;

    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private Long userId;
        private String markdownTheme;
        private Integer safetyCheckMode;
        private Boolean openBlogPush;
        private Integer blogPushMode;

        private Builder() {
        }

        public static Builder aSaveSettingRequest() {
            return new Builder();
        }

        public Builder withUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder withMarkdownTheme(String markdownTheme) {
            this.markdownTheme = markdownTheme;
            return this;
        }

        public Builder withSafetyCheckMode(Integer safetyCheckMode) {
            this.safetyCheckMode = safetyCheckMode;
            return this;
        }

        public Builder withOpenBlogPush(Boolean openBlogPush) {
            this.openBlogPush = openBlogPush;
            return this;
        }

        public Builder withBlogPushMode(Integer blogPushMode) {
            this.blogPushMode = blogPushMode;
            return this;
        }

        public SaveSettingRequest build() {
            SaveSettingRequest saveSettingRequest = new SaveSettingRequest();
            saveSettingRequest.setUserId(userId);
            saveSettingRequest.setMarkdownTheme(markdownTheme);
            saveSettingRequest.setSafetyCheckMode(safetyCheckMode);
            saveSettingRequest.setOpenBlogPush(openBlogPush);
            saveSettingRequest.setBlogPushMode(blogPushMode);
            return saveSettingRequest;
        }
    }
}
