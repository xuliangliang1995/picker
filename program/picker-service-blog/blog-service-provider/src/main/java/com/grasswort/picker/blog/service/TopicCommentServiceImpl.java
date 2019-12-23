package com.grasswort.picker.blog.service;

import com.grasswort.picker.blog.ITopicCommentService;
import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.dao.entity.TopicComment;
import com.grasswort.picker.blog.dao.persistence.TopicCommentMapper;
import com.grasswort.picker.blog.dto.TopicCommentRequest;
import com.grasswort.picker.blog.dto.TopicCommentResponse;
import com.grasswort.picker.blog.dto.TopicCommentsRequest;
import com.grasswort.picker.blog.dto.TopicCommentsResponse;
import com.grasswort.picker.blog.dto.topic.TopicCommentItem;
import com.grasswort.picker.blog.util.TopicIdEncrypt;
import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.user.IUserBaseInfoService;
import com.grasswort.picker.user.dto.UserBaseInfoRequest;
import com.grasswort.picker.user.dto.UserBaseInfoResponse;
import com.grasswort.picker.user.util.PickerIdEncrypt;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xuliangliang
 * @Classname TopicCommentServiceImpl
 * @Description
 * @Date 2019/12/23 15:33
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 10000)
public class TopicCommentServiceImpl implements ITopicCommentService {

    @Autowired TopicCommentMapper topicCommentMapper;

    @Reference(version = "1.0", timeout = 10000)
    IUserBaseInfoService iUserBaseInfoService;

    /**
     * 评分
     *
     * @param commentRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public TopicCommentResponse topicComment(TopicCommentRequest commentRequest) {
        TopicCommentResponse commentResponse = new TopicCommentResponse();
        Long userId = commentRequest.getUserId();
        Long topicId = TopicIdEncrypt.decrypt(commentRequest.getTopicId());
        String content = commentRequest.getContent();
        Integer rate = commentRequest.getRate();

        if (topicId == null) {
            commentResponse.setCode(SysRetCodeConstants.TOPIC_NOT_EXISTS.getCode());
            commentResponse.setMsg(SysRetCodeConstants.TOPIC_NOT_EXISTS.getMsg());
            return commentResponse;
        }

        boolean hasComment = topicCommentMapper.selectIdByUserIdAndTopicId(userId, topicId) != null;
        if (hasComment) {
            commentResponse.setCode(SysRetCodeConstants.REPEATED_COMMENT.getCode());
            commentResponse.setMsg(SysRetCodeConstants.REPEATED_COMMENT.getMsg());
            return commentResponse;
        }

        TopicComment topicComment = new TopicComment();
        topicComment.setUserId(userId);
        topicComment.setTopicId(topicId);
        topicComment.setRate(rate);
        topicComment.setComment(content);
        Date now = new Date();
        topicComment.setGmtCreate(now);
        topicComment.setGmtModified(now);
        topicCommentMapper.insert(topicComment);

        commentResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        commentResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return commentResponse;
    }

    /**
     * 评分列表
     *
     * @param commentsRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public TopicCommentsResponse topicComments(TopicCommentsRequest commentsRequest) {
        TopicCommentsResponse commentsResponse = new TopicCommentsResponse();
        Long userId = commentsRequest.getUserId();
        Long topicId = TopicIdEncrypt.decrypt(commentsRequest.getTopicId());
        Integer pageNo = commentsRequest.getPageNo();
        Integer pageSize = commentsRequest.getPageSize();

        if (topicId == null) {
            commentsResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
            commentsResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
            return commentsResponse;
        }

        Example example = new Example(TopicComment.class);
        example.createCriteria().andEqualTo("topicId", topicId);

        long total = topicCommentMapper.selectCountByExample(example);
        if (total > 0L) {
            if (userId != null) {
                example.setOrderByClause(String.format("if(id = %s, 1, 0) desc, id desc", userId));
            } else {
                example.setOrderByClause("id desc");
            }
            RowBounds rowBounds = new RowBounds(pageSize * (pageNo - 1), pageSize);
            List<TopicComment> comments = topicCommentMapper.selectByExampleAndRowBounds(example, rowBounds);
            List<TopicCommentItem> items = comments.stream().map(comment -> {
                Long pickerId = comment.getUserId();
                UserBaseInfoRequest baseInfoRequest = UserBaseInfoRequest.Builder.anUserBaseInfoRequest()
                        .withUserId(pickerId)
                        .build();
                UserBaseInfoResponse baseInfoResponse = iUserBaseInfoService.userBaseInfo(baseInfoRequest);
                String pickerName = "", pickerAvatar = ""; Integer pickerSex = 0;
                if (baseInfoResponse.isSuccess()) {
                    pickerName = baseInfoResponse.getName();
                    pickerAvatar = baseInfoResponse.getAvatar();
                    pickerSex = (int) baseInfoResponse.getSex();
                }
                TopicCommentItem commentItem = TopicCommentItem.Builder.aTopicCommentItem()
                        .withContent(comment.getComment())
                        .withRate(comment.getRate())
                        .withPickerId(PickerIdEncrypt.encrypt(pickerId))
                        .withGmtCreate(comment.getGmtCreate())
                        .withPickerName(pickerName)
                        .withPickerAvatar(pickerAvatar)
                        .withPickerSex(pickerSex)
                        .build();
                return commentItem;
            }).collect(Collectors.toList());

            commentsResponse.setComments(items);
            commentsResponse.setTotal(total);
            commentsResponse.setHasComment(topicCommentMapper.selectIdByUserIdAndTopicId(userId, topicId) != null);
        } else {
            commentsResponse.setComments(Collections.EMPTY_LIST);
            commentsResponse.setTotal(0L);
            commentsResponse.setHasComment(false);
        }

        commentsResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        commentsResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return commentsResponse;
    }
}
