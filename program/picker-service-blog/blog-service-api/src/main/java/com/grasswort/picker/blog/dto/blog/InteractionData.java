package com.grasswort.picker.blog.dto.blog;

import lombok.Data;

/**
 * @author xuliangliang
 * @Classname InteractionData
 * @Description 交互数据
 * @Date 2019/11/25 17:12
 * @blame Java Team
 */
@Data
public class InteractionData {
    private long like;

    private long favorite;

    private long browse;

    public static final class Builder {
        private long like;
        private long favorite;
        private long browse;

        private Builder() {
        }

        public static Builder anInteractionData() {
            return new Builder();
        }

        public Builder withLike(long like) {
            this.like = like;
            return this;
        }

        public Builder withFavorite(long favorite) {
            this.favorite = favorite;
            return this;
        }

        public Builder withBrowse(long browse) {
            this.browse = browse;
            return this;
        }

        public InteractionData build() {
            InteractionData interactionData = new InteractionData();
            interactionData.setLike(like);
            interactionData.setFavorite(favorite);
            interactionData.setBrowse(browse);
            return interactionData;
        }
    }
}
