package com.grasswort.picker.blog.service;

import com.grasswort.picker.blog.IBlogLikeService;
import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.dao.entity.BlogLike;
import com.grasswort.picker.blog.dao.persistence.BlogLikeMapper;
import com.grasswort.picker.blog.dao.persistence.ext.BlogDao;
import com.grasswort.picker.blog.dto.*;
import com.grasswort.picker.blog.service.elastic.BlogDocUpdateService;
import com.grasswort.picker.blog.util.BlogIdEncrypt;
import com.grasswort.picker.commons.annotation.DB;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

/**
 * @author xuliangliang
 * @Classname BlogLikeServiceImpl
 * @Description 点赞
 * @Date 2019/11/25 14:01
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 10000)
public class BlogLikeServiceImpl implements IBlogLikeService {

    @Autowired BlogLikeMapper blogLikeMapper;

    @Autowired BlogDao blogDao;

    @Autowired BlogDocUpdateService blogDocUpdateService;

    /**
     * 点赞状态
     *
     * @param likeStatusRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public BlogLikeStatusResponse blogLikeStatus(BlogLikeStatusRequest likeStatusRequest) {
        BlogLikeStatusResponse likeStatusResponse = new BlogLikeStatusResponse();

        Long userId = likeStatusRequest.getUserId();
        BlogIdEncrypt.BlogKey blogKey = BlogIdEncrypt.decrypt(likeStatusRequest.getBlogId());

        if (blogKey != null) {
            Example example = new Example(BlogLike.class);
            example.createCriteria()
                    .andEqualTo("userId", userId)
                    .andEqualTo("blogId", blogKey.getBlogId());

            likeStatusResponse.setLike(blogLikeMapper.selectOneByExample(example) != null);
        } else {
            likeStatusResponse.setLike(false);
        }

        likeStatusResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        likeStatusResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return likeStatusResponse;
    }

    /**
     * 点赞
     *
     * @param blogLikeRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public BlogLikeResponse blogLike(BlogLikeRequest blogLikeRequest) {
        BlogLikeResponse likeResponse = new BlogLikeResponse();

        Long userId = blogLikeRequest.getUserId();
        BlogIdEncrypt.BlogKey blogKey = BlogIdEncrypt.decrypt(blogLikeRequest.getBlogId());
        if (blogKey == null) {
            likeResponse.setCode(SysRetCodeConstants.BLOG_NOT_EXISTS.getCode());
            likeResponse.setMsg(SysRetCodeConstants.BLOG_NOT_EXISTS.getMsg());
            return likeResponse;
        }

        Example example = new Example(BlogLike.class);
        example.createCriteria().andEqualTo("userId", userId)
                .andEqualTo("blogId", blogKey.getBlogId());

        boolean likeNotExists = blogLikeMapper.selectOneByExample(example) == null;
        if (likeNotExists) {
            BlogLike like = new BlogLike();
            like.setUserId(userId);
            like.setBlogId(blogKey.getBlogId());
            Date now = new Date(System.currentTimeMillis());
            like.setGmtCreate(now);
            like.setGmtModified(now);
            blogLikeMapper.insert(like);

            // 更新 es 存储
            Long pkUserId = blogDao.getPkUserId(blogKey.getBlogId());
            blogDocUpdateService.updateAuthorDoc(pkUserId, userId);
        }

        likeResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        likeResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return likeResponse;
    }

    /**
     * 取消点赞
     *
     * @param blogLikeCancelRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public BlogLikeCancelResponse blogLikeCancel(BlogLikeCancelRequest blogLikeCancelRequest) {
        BlogLikeCancelResponse likeCancelResponse = new BlogLikeCancelResponse();

        Long userId = blogLikeCancelRequest.getUserId();
        BlogIdEncrypt.BlogKey blogKey = BlogIdEncrypt.decrypt(blogLikeCancelRequest.getBlogId());
        if (blogKey != null) {
            Example example = new Example(BlogLike.class);
            example.createCriteria().andEqualTo("userId", userId)
                    .andEqualTo("blogId", blogKey.getBlogId());

            BlogLike blogLike = blogLikeMapper.selectOneByExample(example);
            if (blogLike != null) {
                blogLikeMapper.deleteByPrimaryKey(blogLike.getId());
                // 更新 es 存储
                Long pkUserId = blogDao.getPkUserId(blogKey.getBlogId());
                blogDocUpdateService.updateAuthorDoc(pkUserId, userId);
            }
        }

        likeCancelResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        likeCancelResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return likeCancelResponse;
    }
}
