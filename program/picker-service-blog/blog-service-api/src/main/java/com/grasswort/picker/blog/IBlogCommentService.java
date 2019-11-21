package com.grasswort.picker.blog;

import com.grasswort.picker.blog.dto.AddCommentRequest;
import com.grasswort.picker.blog.dto.AddCommentResponse;
import com.grasswort.picker.blog.dto.BlogCommentRequest;
import com.grasswort.picker.blog.dto.BlogCommentResponse;

/**
 * @author xuliangliang
 * @Classname IBlogCommentService
 * @Description 评论服务
 * @Date 2019/11/21 20:05
 * @blame Java Team
 */
public interface IBlogCommentService {
    /**
     * 添加评论
     * @param addCommentRequest
     * @return
     */
    AddCommentResponse addComment(AddCommentRequest addCommentRequest);

    /**
     * 评价列表
     * @param commentRequest
     * @return
     */
    BlogCommentResponse comments(BlogCommentRequest commentRequest);
}
