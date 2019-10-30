package com.grasswort.picker.blog.service;

import com.grasswort.picker.blog.IBlogEditService;
import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.dao.entity.Blog;
import com.grasswort.picker.blog.dao.entity.BlogContent;
import com.grasswort.picker.blog.dao.entity.BlogOssRef;
import com.grasswort.picker.blog.dao.persistence.BlogContentMapper;
import com.grasswort.picker.blog.dao.persistence.BlogMapper;
import com.grasswort.picker.blog.dao.persistence.BlogOssRefMapper;
import com.grasswort.picker.blog.dao.persistence.ext.BlogCategoryDao;
import com.grasswort.picker.blog.dto.CreateBlogRequest;
import com.grasswort.picker.blog.dto.CreateBlogResponse;
import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.oss.IOssRefService;
import com.grasswort.picker.oss.constants.OssConstants;
import com.grasswort.picker.oss.dto.OssRefRequest;
import com.grasswort.picker.oss.dto.OssRefResponse;
import com.grasswort.picker.oss.manager.aliyunoss.dto.OssRefDTO;
import com.grasswort.picker.oss.manager.aliyunoss.util.OssUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author xuliangliang
 * @Classname BlogServiceEditServiceImpl
 * @Description 博客服务编辑、创建
 * @Date 2019/10/21 9:15
 * @blame Java Team
 */
@Service(version = "1.0")
public class BlogServiceEditServiceImpl implements IBlogEditService {

    @Autowired BlogMapper blogMapper;

    @Autowired BlogContentMapper blogContentMapper;

    @Autowired BlogCategoryDao blogCategoryDao;

    @Autowired BlogOssRefMapper blogOssRefMapper;

    @Reference(version = "1.0", timeout = 5000) IOssRefService iOssRefService;

    /**
     * 创建博客
     * @param blogRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    @Transactional(rollbackFor = Exception.class)
    public CreateBlogResponse createBlog(CreateBlogRequest blogRequest) {
        CreateBlogResponse createBlogResponse = new CreateBlogResponse();
        final int FIRST_EDITION = 1;

        String title = blogRequest.getTitle();
        String markdown = blogRequest.getMarkdown();
        String html = blogRequest.getHtml();
        Long userId = blogRequest.getUserId();
        Long categoryId = blogRequest.getCategoryId();

        if (categoryId != null && categoryId > 0) {
            // 检测种类是否正确
            Long categoryOwnerId = blogCategoryDao.selectUserIdByPrimaryKey(categoryId);
            boolean categoryNotExists =  categoryOwnerId == null || ! Objects.equals(categoryOwnerId, userId);
            if (categoryNotExists) {
                createBlogResponse.setCode(SysRetCodeConstants.BLOG_CATEGORY_NOT_EXISTS.getCode());
                createBlogResponse.setMsg(SysRetCodeConstants.BLOG_CATEGORY_NOT_EXISTS.getMsg());
                return createBlogResponse;
            }
        }

        // 添加博客
        Blog blog = new Blog();
        blog.setPkUserId(userId);
        blog.setVersion(FIRST_EDITION);
        blog.setTitle(title);
        blog.setCategoryId(categoryId == null ? 0 : categoryId);
        Date now = new Date(System.currentTimeMillis());
        blog.setGmtCreate(now);
        blog.setGmtModified(now);
        blogMapper.insertUseGeneratedKeys(blog);

        BlogContent blogContent = new BlogContent();
        blogContent.setBlogId(blog.getId());
        blogContent.setBlogVersion(FIRST_EDITION);
        blogContent.setTitle(title);
        blogContent.setMarkdown(markdown);
        blogContent.setHtml(html);
        blogContent.setGmtCreate(now);
        blogContent.setGmtModified(now);
        blogContentMapper.insertUseGeneratedKeys(blogContent);

        // 检测 markdown 内容，看是否引用了 oss 图片
        List<OssRefDTO> refs = OssUtils.findOssUrlFromText(markdown);
        refs.forEach(ref -> {
            final String DEL_KEY = RandomStringUtils.random(OssConstants.OSS_DEL_KEY_LENGTH);
            OssRefRequest refRequest = OssRefRequest.Builder.anOssRefRequest()
                    .withBucketName(ref.getBucketName())
                    .withObjectKey(ref.getObjectKey())
                    .withDelKey(DEL_KEY)
                    .build();
            OssRefResponse refResponse = iOssRefService.recordRef(refRequest);

            boolean refSuccess = refResponse != null
                    && com.grasswort.picker.oss.constants.SysRetCodeConstants.SUCCESS.getCode().equals(refResponse.getCode());
            // TODO 为了不影响用户体验，没有引用成功的先记为 0，做定时任务检测失败引用继续尝试引用（或后续改为 kafka 消息队列实现）
            Long refId = refSuccess ? refResponse.getId() : 0;

            BlogOssRef blogOssRef = new BlogOssRef();
            blogOssRef.setBlogId(blog.getId());
            blogOssRef.setOssBucket(ref.getBucketName());
            blogOssRef.setOssKey(ref.getObjectKey());
            blogOssRef.setOssRefId(refId);
            blogOssRef.setOssRefDelKey(DEL_KEY);
            blogOssRef.setGmtCreate(now);
            blogOssRef.setGmtModified(now);
            blogOssRefMapper.insertUseGeneratedKeys(blogOssRef);
        });

        createBlogResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        createBlogResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return createBlogResponse;
    }
}
