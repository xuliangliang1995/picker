package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname DeleteTopicMenuRequest
 * @Description
 * @Date 2019/12/11 22:27
 * @blame Java Team
 */
@Data
public class DeleteTopicMenuRequest extends AbstractRequest {
    @NotNull
    @Min(1)
    private Long menuId;
    @NotNull
    @Min(1)
    private Long pkUserId;

    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private Long menuId;
        private Long pkUserId;

        private Builder() {
        }

        public static Builder aDeleteTopicMenuRequest() {
            return new Builder();
        }

        public Builder withMenuId(Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public Builder withPkUserId(Long pkUserId) {
            this.pkUserId = pkUserId;
            return this;
        }

        public DeleteTopicMenuRequest build() {
            DeleteTopicMenuRequest deleteTopicMenuRequest = new DeleteTopicMenuRequest();
            deleteTopicMenuRequest.setMenuId(menuId);
            deleteTopicMenuRequest.setPkUserId(pkUserId);
            return deleteTopicMenuRequest;
        }
    }
}
