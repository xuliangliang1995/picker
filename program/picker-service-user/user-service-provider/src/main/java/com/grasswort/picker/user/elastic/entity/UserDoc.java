package com.grasswort.picker.user.elastic.entity;

import com.grasswort.picker.user.elastic.constants.EsAnalyzer;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author xuliangliang
 * @Classname UserDoc
 * @Description UserDoc
 * @Date 2019/12/4 11:44
 * @blame Java Team
 */
@Document(indexName = "pk_user", type = "_doc", shards = 3, replicas = 2)
public class UserDoc {
    @Id
    private Long userId;
    /**
     * 昵称
     */
    @Field(type = FieldType.Text, analyzer = EsAnalyzer.IK)
    private String nickName;
    /**
     * 头像
     */
    @Field(type = FieldType.Text, index = false)
    private String avatar;
    /**
     * 签名
     */
    @Field(type = FieldType.Text, analyzer = EsAnalyzer.IK)
    private String signature;
    /**
     * 性别
     */
    @Field(type = FieldType.Integer, index = false)
    private Integer sex;

    /**
     * 博客数量
     */
    @Field(type = FieldType.Long)
    private Long blogCount;
    /**
     * 关注别人的数量
     */
    @Field(type = FieldType.Long)
    private Long subscribeCount;
    /**
     * 粉丝
     */
    @Field(type = FieldType.Long)
    private Long fansCount;
    /**
     * 获赞
     */
    @Field(type = FieldType.Long)
    private Long likedCount;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Long getBlogCount() {
        return blogCount;
    }

    public void setBlogCount(Long blogCount) {
        this.blogCount = blogCount;
    }

    public Long getSubscribeCount() {
        return subscribeCount;
    }

    public void setSubscribeCount(Long subscribeCount) {
        this.subscribeCount = subscribeCount;
    }

    public Long getFansCount() {
        return fansCount;
    }

    public void setFansCount(Long fansCount) {
        this.fansCount = fansCount;
    }

    public Long getLikedCount() {
        return likedCount;
    }

    public void setLikedCount(Long likedCount) {
        this.likedCount = likedCount;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public static final class Builder {
        private Long userId;
        private String nickName;
        private String avatar;
        private String signature;
        private Integer sex;
        private Long blogCount;
        private Long subscribeCount;
        private Long fansCount;
        private Long likedCount;

        private Builder() {
        }

        public static Builder anUserDoc() {
            return new Builder();
        }

        public Builder withUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder withNickName(String nickName) {
            this.nickName = nickName;
            return this;
        }

        public Builder withAvatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public Builder withSignature(String signature) {
            this.signature = signature;
            return this;
        }

        public Builder withSex(Integer sex) {
            this.sex = sex;
            return this;
        }

        public Builder withBlogCount(Long blogCount) {
            this.blogCount = blogCount;
            return this;
        }

        public Builder withSubscribeCount(Long subscribeCount) {
            this.subscribeCount = subscribeCount;
            return this;
        }

        public Builder withFansCount(Long fansCount) {
            this.fansCount = fansCount;
            return this;
        }

        public Builder withLikedCount(Long likedCount) {
            this.likedCount = likedCount;
            return this;
        }

        public UserDoc build() {
            UserDoc userDoc = new UserDoc();
            userDoc.setUserId(userId);
            userDoc.setNickName(nickName);
            userDoc.setAvatar(avatar);
            userDoc.setSignature(signature);
            userDoc.setSex(sex);
            userDoc.setBlogCount(blogCount);
            userDoc.setSubscribeCount(subscribeCount);
            userDoc.setFansCount(fansCount);
            userDoc.setLikedCount(likedCount);
            return userDoc;
        }
    }
}
