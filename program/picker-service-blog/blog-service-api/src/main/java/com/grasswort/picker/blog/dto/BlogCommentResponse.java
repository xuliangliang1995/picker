package com.grasswort.picker.blog.dto;

import com.grasswort.picker.blog.dto.comment.CommentItem;
import lombok.Data;

import java.util.List;

/**
 * @author xuliangliang
 * @Classname BlogCommentResponse
 * @Description 评价
 * @Date 2019/11/21 22:24
 * @blame Java Team
 */
@Data
public class BlogCommentResponse extends AbstractBlogResponse {

    private List<CommentItem> comments;

    private Long total;
}
