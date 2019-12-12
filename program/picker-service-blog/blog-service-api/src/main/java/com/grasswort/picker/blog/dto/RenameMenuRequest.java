package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author xuliangliang
 * @Classname RenameMenuRequest
 * @Description 菜单重命名
 * @Date 2019/12/12 15:00
 * @blame Java Team
 */
@Data
public class RenameMenuRequest extends AbstractRequest {
    @NotNull
    @Min(1)
    private Long pkUserId;
    @NotEmpty
    private String topicId;
    @NotNull
    @Min(1)
    private Long menuId;
    @NotEmpty
    @Size(max = 50)
    private String menuName;
    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private Long pkUserId;
        private String topicId;
        private Long menuId;
        private String menuName;

        private Builder() {
        }

        public static Builder aRenameMenuRequest() {
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

        public Builder withMenuName(String menuName) {
            this.menuName = menuName;
            return this;
        }

        public RenameMenuRequest build() {
            RenameMenuRequest renameMenuRequest = new RenameMenuRequest();
            renameMenuRequest.setPkUserId(pkUserId);
            renameMenuRequest.setTopicId(topicId);
            renameMenuRequest.setMenuId(menuId);
            renameMenuRequest.setMenuName(menuName);
            return renameMenuRequest;
        }
    }
}
