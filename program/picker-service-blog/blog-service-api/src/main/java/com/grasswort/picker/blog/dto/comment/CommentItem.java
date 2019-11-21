package com.grasswort.picker.blog.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xuliangliang
 * @Classname CommentItem
 * @Description 评论
 * @Date 2019/11/21 21:59
 * @blame Java Team
 */
@Data
public class CommentItem {

    private Long commentId;

    private Long replyCommentId;

    private String commentContent;

    private String userId;

    private String userName;

    private String userAvatar;

    private Integer replyCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;

    private List<CommentItem> replyComments;


    public static final class Builder {
        private Long commentId;
        private Long replyCommentId;
        private String commentContent;
        private String userId;
        private String userName;
        private String userAvatar;
        private Integer replyCount;
        private Date gmtCreate;
        private List<CommentItem> replyComments;

        private Builder() {
        }

        public static Builder aCommentItem() {
            return new Builder();
        }

        public Builder withCommentId(Long commentId) {
            this.commentId = commentId;
            return this;
        }

        public Builder withReplyCommentId(Long replyCommentId) {
            this.replyCommentId = replyCommentId;
            return this;
        }

        public Builder withCommentContent(String commentContent) {
            this.commentContent = commentContent;
            return this;
        }

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder withUserAvatar(String userAvatar) {
            this.userAvatar = userAvatar;
            return this;
        }

        public Builder withReplyCount(Integer replyCount) {
            this.replyCount = replyCount;
            return this;
        }

        public Builder withGmtCreate(Date gmtCreate) {
            this.gmtCreate = gmtCreate;
            return this;
        }

        public Builder withReplyComments(List<CommentItem> replyComments) {
            this.replyComments = replyComments;
            return this;
        }

        public CommentItem build() {
            CommentItem commentItem = new CommentItem();
            commentItem.setCommentId(commentId);
            commentItem.setReplyCommentId(replyCommentId);
            commentItem.setCommentContent(commentContent);
            commentItem.setUserId(userId);
            commentItem.setUserName(userName);
            commentItem.setUserAvatar(userAvatar);
            commentItem.setReplyCount(replyCount);
            commentItem.setGmtCreate(gmtCreate);
            commentItem.setReplyComments(replyComments);
            return commentItem;
        }
    }
}
