package com.grasswort.picker.blog.service;

import com.grasswort.picker.blog.IBlogFavoriteService;
import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.dao.entity.BlogFavorite;
import com.grasswort.picker.blog.dao.persistence.BlogFavoriteMapper;
import com.grasswort.picker.blog.dao.persistence.ext.BlogDao;
import com.grasswort.picker.blog.dto.*;
import com.grasswort.picker.blog.util.BlogIdEncrypt;
import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.user.IUserElasticDocUpdateService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.Optional;

/**
 * @author xuliangliang
 * @Classname BlogFavoriteServiceImpl
 * @Description 收藏
 * @Date 2019/11/25 14:14
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 10000)
public class BlogFavoriteServiceImpl implements IBlogFavoriteService {

    @Autowired BlogFavoriteMapper blogFavoriteMapper;

    @Autowired BlogDao blogDao;

    @Reference(version = "1.0", timeout = 10000)
    IUserElasticDocUpdateService iUserElasticDocUpdateService;

    /**
     * 博客收藏状态
     *
     * @param blogFavoriteStatusRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public BlogFavoriteStatusResponse blogFavoriteStatus(BlogFavoriteStatusRequest blogFavoriteStatusRequest) {
        BlogFavoriteStatusResponse favoriteStatusResponse = new BlogFavoriteStatusResponse();

        Long userId = blogFavoriteStatusRequest.getUserId();
        BlogIdEncrypt.BlogKey blogKey = BlogIdEncrypt.decrypt(blogFavoriteStatusRequest.getBlogId());

        BlogFavorite favorite = Optional.ofNullable(blogKey)
                .map(BlogIdEncrypt.BlogKey::getBlogId)
                .map(blogId -> {
                    Example example = new Example(BlogFavorite.class);
                    example.createCriteria().andEqualTo("userId", userId)
                            .andEqualTo("blogId", blogId);

                    return blogFavoriteMapper.selectOneByExample(example);
                })
                .orElse(null);

        favoriteStatusResponse.setFavorite(favorite != null);
        favoriteStatusResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        favoriteStatusResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return favoriteStatusResponse;
    }

    /**
     * 收藏
     *
     * @param favoriteRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public BlogFavoriteResponse blogFavorite(BlogFavoriteRequest favoriteRequest) {
        BlogFavoriteResponse favoriteResponse = new BlogFavoriteResponse();

        Long userId = favoriteRequest.getUserId();
        BlogIdEncrypt.BlogKey blogKey = BlogIdEncrypt.decrypt(favoriteRequest.getBlogId());

        if (blogKey == null) {
            favoriteResponse.setCode(SysRetCodeConstants.BLOG_NOT_EXISTS.getCode());
            favoriteResponse.setMsg(SysRetCodeConstants.BLOG_NOT_EXISTS.getMsg());
            return favoriteResponse;
        }

        Example example = new Example(BlogFavorite.class);
        example.createCriteria().andEqualTo("userId", userId)
                .andEqualTo("blogId", blogKey.getBlogId());
        boolean favoriteNotExists = blogFavoriteMapper.selectOneByExample(example) == null;
        if (favoriteNotExists) {
            BlogFavorite favorite = new BlogFavorite();
            favorite.setUserId(userId);
            favorite.setBlogId(blogKey.getBlogId());
            Date now = new Date(System.currentTimeMillis());
            favorite.setGmtCreate(now);
            favorite.setGmtModified(now);
            blogFavoriteMapper.insert(favorite);

            // 更新 es 存储
            Long pkUserId = blogDao.getPkUserId(blogKey.getBlogId());
            iUserElasticDocUpdateService.updateElastic(pkUserId);
            iUserElasticDocUpdateService.updateElastic(userId);
        }

        favoriteResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        favoriteResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return favoriteResponse;
    }

    /**
     * 取消收藏
     *
     * @param favoriteCancelRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public BlogFavoriteCancelResponse blogFavoriteCancel(BlogFavoriteCancelRequest favoriteCancelRequest) {
        BlogFavoriteCancelResponse favoriteCancelResponse =  new BlogFavoriteCancelResponse();

        Long userId = favoriteCancelRequest.getUserId();
        BlogIdEncrypt.BlogKey blogKey = BlogIdEncrypt.decrypt(favoriteCancelRequest.getBlogId());

        BlogFavorite favorite = Optional.ofNullable(blogKey)
                .map(BlogIdEncrypt.BlogKey::getBlogId)
                .map(blogId -> {
                    Example example = new Example(BlogFavorite.class);
                    example.createCriteria().andEqualTo("userId", userId)
                            .andEqualTo("blogId", blogId);

                    return blogFavoriteMapper.selectOneByExample(example);
                })
                .orElse(null);

        if (favorite != null) {
            blogFavoriteMapper.deleteByPrimaryKey(favorite.getId());
            // 更新 es 存储
            Long pkUserId = blogDao.getPkUserId(blogKey.getBlogId());
            iUserElasticDocUpdateService.updateElastic(pkUserId);
            iUserElasticDocUpdateService.updateElastic(userId);
        }

        favoriteCancelResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        favoriteCancelResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return favoriteCancelResponse;
    }
}
