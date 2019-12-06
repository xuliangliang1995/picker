package com.grasswort.picker.user.dto.user;

import lombok.Data;

/**
 * @author xuliangliang
 * @Classname UserItem
 * @Description 用户
 * @Date 2019/12/3 22:55
 * @blame Java Team
 */
@Data
public class UserItem {

    private String userId;

    private String nickName;

    private String signature;

    private String avatar;

    private Byte sex;

    private Boolean subscribe;

    private InteractionData interactionData;


    public static final class Builder {
        private String userId;
        private String nickName;
        private String signature;
        private String avatar;
        private Byte sex;
        private Boolean subscribe;
        private InteractionData interactionData;

        private Builder() {
        }

        public static Builder anUserItem() {
            return new Builder();
        }

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withNickName(String nickName) {
            this.nickName = nickName;
            return this;
        }

        public Builder withSignature(String signature) {
            this.signature = signature;
            return this;
        }

        public Builder withAvatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public Builder withSex(Byte sex) {
            this.sex = sex;
            return this;
        }

        public Builder withSubscribe(Boolean subscribe) {
            this.subscribe = subscribe;
            return this;
        }

        public Builder withInteractionData(InteractionData interactionData) {
            this.interactionData = interactionData;
            return this;
        }

        public UserItem build() {
            UserItem userItem = new UserItem();
            userItem.setUserId(userId);
            userItem.setNickName(nickName);
            userItem.setSignature(signature);
            userItem.setAvatar(avatar);
            userItem.setSex(sex);
            userItem.setSubscribe(subscribe);
            userItem.setInteractionData(interactionData);
            return userItem;
        }
    }
}
