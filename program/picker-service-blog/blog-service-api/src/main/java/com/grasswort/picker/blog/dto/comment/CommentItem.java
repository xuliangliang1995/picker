package com.grasswort.picker.blog.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Builder
@NoArgsConstructor
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
}
