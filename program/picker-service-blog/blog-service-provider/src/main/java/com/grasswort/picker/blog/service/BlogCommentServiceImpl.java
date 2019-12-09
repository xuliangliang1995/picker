package com.grasswort.picker.blog.service;

import com.grasswort.picker.blog.IBlogCommentService;
import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.dao.entity.BlogComment;
import com.grasswort.picker.blog.dao.entity.BlogCommentContent;
import com.grasswort.picker.blog.dao.persistence.BlogCommentContentMapper;
import com.grasswort.picker.blog.dao.persistence.BlogCommentMapper;
import com.grasswort.picker.blog.dto.AddCommentRequest;
import com.grasswort.picker.blog.dto.AddCommentResponse;
import com.grasswort.picker.blog.dto.BlogCommentRequest;
import com.grasswort.picker.blog.dto.BlogCommentResponse;
import com.grasswort.picker.blog.dto.comment.CommentItem;
import com.grasswort.picker.blog.service.elastic.BlogDocUpdateService;
import com.grasswort.picker.blog.util.BlogIdEncrypt;
import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.user.IUserBaseInfoService;
import com.grasswort.picker.user.dto.UserBaseInfoRequest;
import com.grasswort.picker.user.dto.UserBaseInfoResponse;
import com.grasswort.picker.user.util.PickerIdEncrypt;
import com.mchange.v2.holders.ThreadSafeLongHolder;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xuliangliang
 * @Classname BlogCommentServiceImpl
 * @Description 评论
 * @Date 2019/11/21 21:42
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 10000)
public class BlogCommentServiceImpl implements IBlogCommentService {

    @Autowired BlogCommentMapper blogCommentMapper;

    @Autowired BlogCommentContentMapper blogCommentContentMapper;

    @Autowired BlogDocUpdateService blogDocUpdateService;

    @Reference(version = "1.0", timeout = 10000) IUserBaseInfoService iUserBaseInfoService;

    private ThreadLocal<Map<String, UserBaseInfoResponse>> userInfoHolder = new ThreadLocal<>();

    /**
     * 添加评论
     *
     * @param addCommentRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    @Transactional(rollbackFor = Exception.class)
    public AddCommentResponse addComment(AddCommentRequest addCommentRequest) {
        AddCommentResponse addCommentResponse = new AddCommentResponse();

        BlogIdEncrypt.BlogKey blogKey = BlogIdEncrypt.decrypt(addCommentRequest.getBlogId());
        Long userId = addCommentRequest.getUserId();
        Long replyCommentId = addCommentRequest.getReplyCommentId();

        if (replyCommentId > 0 && ! blogCommentMapper.existsWithPrimaryKey(replyCommentId)) {
            addCommentResponse.setMsg(SysRetCodeConstants.COMMENT_NOT_EXISTS.getMsg());
            addCommentResponse.setCode(SysRetCodeConstants.COMMENT_NOT_EXISTS.getCode());
            return addCommentResponse;
        }
        Date now = new Date(System.currentTimeMillis());
        // 添加评论
        BlogComment blogComment = new BlogComment();
        blogComment.setPkBlogId(blogKey.getBlogId());
        blogComment.setPkUserId(userId);
        blogComment.setReplyCommentId(replyCommentId);
        blogComment.setGmtCreate(now);
        blogComment.setGmtModified(now);
        blogCommentMapper.insertUseGeneratedKeys(blogComment);
        // 添加评论内容
        BlogCommentContent commentContent = new BlogCommentContent();
        commentContent.setCommentId(blogComment.getId());
        commentContent.setContent(addCommentRequest.getContent());
        commentContent.setGmtCreate(now);
        commentContent.setGmtModified(now);
        blogCommentContentMapper.insertUseGeneratedKeys(commentContent);
        // 更新博客 ES 存储
        blogDocUpdateService.updateBlogDoc(blogKey.getBlogId());

        addCommentResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        addCommentResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        return addCommentResponse;
    }

    /**
     * 评价列表
     *
     * @param commentRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public BlogCommentResponse comments(BlogCommentRequest commentRequest) {
        BlogCommentResponse commentResponse = new BlogCommentResponse();
        BlogIdEncrypt.BlogKey blogKey = BlogIdEncrypt.decrypt(commentRequest.getBlogId());

        Long blogId = blogKey.getBlogId();
        Integer pageNo = commentRequest.getPageNo();
        Integer pageSize = commentRequest.getPageSize();

        Example example = new Example(BlogComment.class);
        example.createCriteria()
                .andEqualTo("pkBlogId", blogId)
                .andEqualTo("replyCommentId", 0);
        example.setOrderByClause("id desc");

        RowBounds rowBounds = new RowBounds(pageSize * (pageNo - 1), pageSize);

        long total = blogCommentMapper.selectCountByExample(example);
        if (total > 0) {
            List<CommentItem> comments = blogCommentMapper.selectByExampleAndRowBounds(example, rowBounds).stream()
                    .map(comment -> {
                        String content = blogCommentContentMapper.getContent(comment.getId());
                        CommentItem item = CommentItem.Builder.aCommentItem()
                                .withCommentId(comment.getId())
                                .withUserId(PickerIdEncrypt.encrypt(comment.getPkUserId()))
                                .withReplyCommentId(comment.getReplyCommentId())
                                .withCommentContent(content)
                                .withGmtCreate(comment.getGmtCreate())
                                .build();
                        return item;
                    }).collect(Collectors.toList());
            this.replenishReplyAndUserInfo(comments);
            commentResponse.setComments(comments);
        } else {
            commentResponse.setComments(Collections.EMPTY_LIST);
        }
        commentResponse.setTotal(total);
        commentResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        commentResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        return commentResponse;
    }

    /**
     * 补充回复和用户信息
     * @param items
     */
    private void replenishReplyAndUserInfo(List<CommentItem> items) {
        for (CommentItem item: items) {
            Example example = new Example(BlogComment.class);
            example.createCriteria().andEqualTo("replyCommentId", item.getCommentId());
            example.setOrderByClause("id asc");
            // 补充用户信息
            UserBaseInfoResponse baseInfoResponse = Optional.ofNullable(userInfoHolder.get())
                    .map(m -> m.get(item.getUserId())).orElse(null);
            if (baseInfoResponse == null) {
                baseInfoResponse= iUserBaseInfoService.userBaseInfo(
                        UserBaseInfoRequest.Builder.anUserBaseInfoRequest()
                                .withUserId(PickerIdEncrypt.decrypt(item.getUserId()))
                                .build()
                );
                if (baseInfoResponse != null && baseInfoResponse.isSuccess()) {
                    if (userInfoHolder.get() == null) {
                        userInfoHolder.set(new HashMap<>());
                    }
                    userInfoHolder.get().put(item.getUserId(), baseInfoResponse);
                }
            }
            if (baseInfoResponse != null && baseInfoResponse.isSuccess()) {
                item.setUserName(baseInfoResponse.getName());
                item.setUserAvatar(baseInfoResponse.getAvatar());
            }
            // 补充回复信息
            List<CommentItem> comments = blogCommentMapper.selectByExample(example).stream()
                    .map(comment -> {
                        String content = blogCommentContentMapper.getContent(comment.getId());
                        CommentItem citem = CommentItem.Builder.aCommentItem()
                                .withCommentId(comment.getId())
                                .withUserId(PickerIdEncrypt.encrypt(comment.getPkUserId()))
                                .withReplyCommentId(comment.getReplyCommentId())
                                .withCommentContent(content)
                                .withGmtCreate(comment.getGmtCreate())
                                .build();
                        return citem;
                    }).collect(Collectors.toList());
            // 递归补充信息
            this.replenishReplyAndUserInfo(comments);
            item.setReplyComments(comments);
        }
    }
}
