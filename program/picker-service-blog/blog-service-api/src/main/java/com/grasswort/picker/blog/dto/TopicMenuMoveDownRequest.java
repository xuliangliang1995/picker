package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname TopicMenuMoveDownRequest
 * @Description 菜单下移
 * @Date 2019/12/12 14:37
 * @blame Java Team
 */
@Data
public class TopicMenuMoveDownRequest extends AbstractRequest {
    @Min(1)
    @NotNull
    private Long pkUserId;
    @NotEmpty
    private String topicId;
    @NotNull
    @Min(1)
    private Long menuId;

    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private Long pkUserId;
        private String topicId;
        private Long menuId;

        private Builder() {
        }

        public static Builder aTopicMenuMoveDownRequest() {
            return new Builder();
        }

        public Builder withPkUserId(Long pkUserId) {
            this.pkUserId = pkUserId;
            return this;
        }

        public Builder withTopicId(String topicId) {
            this.topicId = topicId;
            return this;
        }

        public Builder withMenuId(Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public TopicMenuMoveDownRequest build() {
            TopicMenuMoveDownRequest topicMenuMoveDownRequest = new TopicMenuMoveDownRequest();
            topicMenuMoveDownRequest.setPkUserId(pkUserId);
            topicMenuMoveDownRequest.setTopicId(topicId);
            topicMenuMoveDownRequest.setMenuId(menuId);
            return topicMenuMoveDownRequest;
        }
    }
}
