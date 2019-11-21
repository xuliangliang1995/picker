package com.grasswort.picker.blog.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname CommentForm
 * @Description 评论
 * @Date 2019/11/21 23:09
 * @blame Java Team
 */
@Data
public class CommentForm {
    @Min(0)
    @NotNull
    private Long replyCommentId;
    @NotEmpty
    private String content;
}
