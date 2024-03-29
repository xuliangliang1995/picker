package com.grasswort.picker.blog.service;

import com.grasswort.picker.blog.IBlogFavoriteService;
import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.dao.entity.BlogFavorite;
import com.grasswort.picker.blog.dao.persistence.BlogFavoriteMapper;
import com.grasswort.picker.blog.dao.persistence.ext.BlogDao;
import com.grasswort.picker.blog.dto.*;
import com.grasswort.picker.blog.elastic.entity.BlogDoc;
import com.grasswort.picker.blog.elastic.repository.BlogDocRepository;
import com.grasswort.picker.blog.service.elastic.BlogDocConverter;
import com.grasswort.picker.blog.service.elastic.BlogDocUpdateService;
import com.grasswort.picker.blog.service.redisson.BlogFavoriteCacheable;
import com.grasswort.picker.blog.util.BlogIdEncrypt;
import com.grasswort.picker.commons.annotation.DB;
import org.apache.dubbo.config.annotation.Service;
import org.redisson.api.RList;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired BlogDocUpdateService blogDocUpdateService;

    @Autowired BlogFavoriteCacheable blogFavoriteCacheable;

    @Autowired BlogDocRepository blogDocRepository;

    @Autowired BlogDocConverter blogDocConverter;

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
            blogDocUpdateService.updateAuthorDoc(pkUserId, userId);
            blogFavoriteCacheable.clear(userId);
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
            blogDocUpdateService.updateAuthorDoc(pkUserId, userId);
            blogFavoriteCacheable.clear(userId);
        }

        favoriteCancelResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        favoriteCancelResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return favoriteCancelResponse;
    }

    /**
     * 博客收藏列表
     *
     * @param favoriteListRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public BlogFavoriteListResponse listBlogFavorite(BlogFavoriteListRequest favoriteListRequest) {
        BlogFavoriteListResponse favoriteListResponse = new BlogFavoriteListResponse();
        Long authorId = favoriteListRequest.getAuthorId();
        Integer pageNo = favoriteListRequest.getPageNo();
        Integer pageSize = favoriteListRequest.getPageSize();

        RList<Long> rBlogIds = blogFavoriteCacheable.listFavorite(authorId);
        if (rBlogIds == null) {
            List<Long> blogIds = blogFavoriteMapper.listBlogIdFavorite(authorId);
            blogFavoriteCacheable.cacheUserFavorite(authorId, blogIds);
            rBlogIds = blogFavoriteCacheable.listFavorite(authorId);
        }

        if (rBlogIds != null) {
            favoriteListResponse.setTotal(Long.valueOf(rBlogIds.size()));
            favoriteListResponse.setBlogList(
                    rBlogIds.range(pageSize * (pageNo - 1), pageNo * pageSize - 1)
                    .stream().map(blogId -> {
                        Optional<BlogDoc> blogDocOpt = blogDocRepository.findById(blogId);
                        if (blogDocOpt.isPresent()) {
                            return blogDocConverter.blogDoc2BlogItemWithAuthor(blogDocOpt.get());
                        }
                        return null;
                    }).filter(r -> r != null).collect(Collectors.toList())
            );
        } else {
            favoriteListResponse.setTotal(0L);
            favoriteListResponse.setBlogList(Collections.EMPTY_LIST);
        }
        favoriteListResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        favoriteListResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return favoriteListResponse;
    }
}
