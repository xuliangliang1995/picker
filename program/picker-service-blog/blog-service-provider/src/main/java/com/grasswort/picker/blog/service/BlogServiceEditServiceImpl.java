package com.grasswort.picker.blog.service;

import com.grasswort.picker.blog.IBlogEditService;
import com.grasswort.picker.blog.constant.BlogCurveStatusEnum;
import com.grasswort.picker.blog.constant.BlogStatusEnum;
import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.dao.entity.Blog;
import com.grasswort.picker.blog.dao.entity.BlogContent;
import com.grasswort.picker.blog.dao.entity.BlogLabel;
import com.grasswort.picker.blog.dao.entity.BlogOssRef;
import com.grasswort.picker.blog.dao.persistence.BlogContentMapper;
import com.grasswort.picker.blog.dao.persistence.BlogLabelMapper;
import com.grasswort.picker.blog.dao.persistence.BlogMapper;
import com.grasswort.picker.blog.dao.persistence.BlogOssRefMapper;
import com.grasswort.picker.blog.dao.persistence.ext.BlogCategoryDao;
import com.grasswort.picker.blog.dao.persistence.ext.BlogLabelDao;
import com.grasswort.picker.blog.dto.*;
import com.grasswort.picker.blog.util.BlogIdEncrypt;
import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.commons.config.DBLocalHolder;
import com.grasswort.picker.oss.IOssRefService;
import com.grasswort.picker.oss.constants.OssConstants;
import com.grasswort.picker.oss.dto.OssRefRequest;
import com.grasswort.picker.oss.dto.OssRefResponse;
import com.grasswort.picker.oss.manager.aliyunoss.dto.OssRefDTO;
import com.grasswort.picker.oss.manager.aliyunoss.util.OssUtils;
import com.grasswort.picker.user.IUserBaseInfoService;
import com.grasswort.picker.user.IUserSettingService;
import com.grasswort.picker.user.dto.BlogPushSettingRequest;
import com.grasswort.picker.user.dto.BlogPushSettingResponse;
import com.grasswort.picker.user.dto.UserBaseInfoRequest;
import com.grasswort.picker.user.dto.UserBaseInfoResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @author xuliangliang
 * @Classname BlogServiceEditServiceImpl
 * @Description 博客服务编辑、创建
 * @Date 2019/10/21 9:15
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 10000)
public class BlogServiceEditServiceImpl implements IBlogEditService {

    @Autowired BlogMapper blogMapper;

    @Autowired BlogContentMapper blogContentMapper;

    @Autowired BlogCategoryDao blogCategoryDao;

    @Autowired BlogOssRefMapper blogOssRefMapper;

    @Autowired BlogLabelMapper blogLabelMapper;

    @Autowired BlogLabelDao blogLabelDao;

    @Autowired RetentionCurveServiceImpl retentionCurveServiceImpl;

    @Reference(version = "1.0", timeout = 10000) IUserSettingService iUserSettingService;

    @Reference(version = "1.0", timeout = 10000) IOssRefService iOssRefService;
    /**
     * 首次创建文章版本号
     */
    private final static int FIRST_EDITION = 1;

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

        String title = blogRequest.getTitle();
        String markdown = blogRequest.getMarkdown();
        String html = blogRequest.getHtml();
        Long userId = blogRequest.getUserId();
        Long categoryId = blogRequest.getCategoryId();
        String coverImg = blogRequest.getCoverImg();
        String summary = blogRequest.getSummary();
        Set<String> labels = blogRequest.getLabels();

        // 判断分类是否正确
        boolean categoryNotCorrect =  ! judgeCategoryIsExists(userId, categoryId);
        if (categoryNotCorrect) {
            createBlogResponse.setCode(SysRetCodeConstants.BLOG_CATEGORY_NOT_EXISTS.getCode());
            createBlogResponse.setMsg(SysRetCodeConstants.BLOG_CATEGORY_NOT_EXISTS.getMsg());
            return createBlogResponse;
        }

        // 添加博客
        Blog blog = new Blog();
        blog.setStatus(BlogStatusEnum.NORMAL.status());
        blog.setPkUserId(userId);
        blog.setVersion(FIRST_EDITION);
        blog.setTitle(title);
        blog.setCategoryId(categoryId == null ? 0 : categoryId);
        blog.setCoverImg(coverImg);
        blog.setSummary(summary);
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
        Set<OssRefDTO> refs = OssUtils.findOssUrlFromText(markdown);
        refs.add(OssUtils.resolverUrl(coverImg));
        processRef(refs);

        // 存储标签
        processLabels(blog.getId(), labels);

        BlogPushSettingResponse pushSettingResponse = iUserSettingService.getBlogPushSetting(new BlogPushSettingRequest(userId));

        boolean openPush = Optional.ofNullable(pushSettingResponse)
                .filter(BlogPushSettingResponse::isSuccess)
                .map(BlogPushSettingResponse::getOpenBlogPush)
                .orElse(false);
        if (openPush) {
            // 开启记忆曲线
            retentionCurveServiceImpl.blogCurvePatch(
                    BlogCurveRequest.Builder.aBlogCurveRequest()
                            .withBlogId(BlogIdEncrypt.encrypt(blog.getId()))
                            .withUserId(userId)
                            .withOrder(1)
                            .withStatus(BlogCurveStatusEnum.NORMAL)
                            .build()
            );
        }

        createBlogResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        createBlogResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return createBlogResponse;
    }

    /**
     * 编辑博客
     *
     * @param editBlogRequest
     * @return
     */
    @DB(DBGroup.MASTER)
    @Override
    public EditBlogResponse editBlog(EditBlogRequest editBlogRequest) {
        EditBlogResponse editBlogResponse = new EditBlogResponse();

        String title = editBlogRequest.getTitle();
        String markdown = editBlogRequest.getMarkdown();
        String html = editBlogRequest.getHtml();
        Long userId = editBlogRequest.getUserId();
        Long categoryId = editBlogRequest.getCategoryId();
        String coverImg = editBlogRequest.getCoverImg();
        String summary = editBlogRequest.getSummary();
        Set<String> labels = editBlogRequest.getLabels();

        BlogIdEncrypt.BlogKey blogKey = BlogIdEncrypt.decrypt(editBlogRequest.getBlogId());
        Blog blog = Optional.ofNullable(blogKey).map(BlogIdEncrypt.BlogKey::getBlogId)
                .map(blogMapper::selectByPrimaryKey)
                .filter(b -> Objects.equals(b.getPkUserId(), userId))
                .orElse(null);

        if (blog == null) {
            editBlogResponse.setCode(SysRetCodeConstants.BLOG_NOT_EXISTS.getCode());
            editBlogResponse.setMsg(SysRetCodeConstants.BLOG_NOT_EXISTS.getMsg());
            return editBlogResponse;
        }

        boolean categoryNotCorrect =  ! judgeCategoryIsExists(userId, categoryId);
        if (categoryNotCorrect) {
            editBlogResponse.setCode(SysRetCodeConstants.BLOG_CATEGORY_NOT_EXISTS.getCode());
            editBlogResponse.setMsg(SysRetCodeConstants.BLOG_CATEGORY_NOT_EXISTS.getMsg());
            return editBlogResponse;
        }

        blog.setTitle(title);
        blog.setCategoryId(categoryId == null ? 0 : categoryId);
        blog.setCoverImg(coverImg);
        blog.setSummary(summary);
        Date now = new Date(System.currentTimeMillis());
        blog.setGmtModified(now);
        blogMapper.updateByPrimaryKey(blog);

        final int VERSION = blogKey.getVersion() > 0 ? blogKey.getVersion() : blog.getVersion();

        Example example = new Example(BlogContent.class);
        example.createCriteria().andEqualTo("blogId", blog.getId())
                .andEqualTo("blogVersion", VERSION);

        BlogContent content = blogContentMapper.selectOneByExample(example);

        BlogContent blogContentSelective = new BlogContent();
        blogContentSelective.setId(content.getId());
        blogContentSelective.setMarkdown(markdown);
        blogContentSelective.setHtml(html);
        blogContentSelective.setGmtModified(now);

        blogContentMapper.updateByPrimaryKeySelective(blogContentSelective);

        // 检测 markdown 内容，看是否引用了 oss 图片
        Set<OssRefDTO> refs = OssUtils.findOssUrlFromText(markdown);
        refs.add(OssUtils.resolverUrl(coverImg));
        processRef(refs);

        // 存储标签
        processLabels(blog.getId(), labels);

        editBlogResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        editBlogResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return editBlogResponse;
    }

    /**
     * 修改博客分类
     * @param changeBlogCategoryRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public ChangeBlogCategoryResponse changeBlogCategory(ChangeBlogCategoryRequest changeBlogCategoryRequest) {
        ChangeBlogCategoryResponse changeBlogCategoryResponse = new ChangeBlogCategoryResponse();

        Long categoryId = changeBlogCategoryRequest.getCategoryId();
        // 判断分类是否正确
        boolean categoryNotCorrect =  ! judgeCategoryIsExists(changeBlogCategoryRequest.getUserId(), categoryId);
        if (categoryNotCorrect) {
            changeBlogCategoryResponse.setCode(SysRetCodeConstants.BLOG_CATEGORY_NOT_EXISTS.getCode());
            changeBlogCategoryResponse.setMsg(SysRetCodeConstants.BLOG_CATEGORY_NOT_EXISTS.getMsg());
            return changeBlogCategoryResponse;
        }
        // 判断博客是否存在，；以及是否有权限修改博客分类
        BlogIdEncrypt.BlogKey blogKey = BlogIdEncrypt.decrypt(changeBlogCategoryRequest.getBlogId());
        Blog blog = Optional.ofNullable(blogKey)
                .map(BlogIdEncrypt.BlogKey::getBlogId)
                .map(blogMapper::selectByPrimaryKey)
                .filter(b -> Objects.equals(b.getStatus(), BlogStatusEnum.NORMAL.status()))
                .filter(b -> Objects.equals(b.getPkUserId(), changeBlogCategoryRequest.getUserId()))
                .orElse(null);
        if (blog == null) {
            changeBlogCategoryResponse.setCode(SysRetCodeConstants.BLOG_NOT_EXISTS.getCode());
            changeBlogCategoryResponse.setMsg(SysRetCodeConstants.BLOG_NOT_EXISTS.getMsg());
            return changeBlogCategoryResponse;
        }
        // 开始修改
        DBLocalHolder.selectDBGroup(DBGroup.MASTER);

        Blog blogSelective = new Blog();
        blogSelective.setId(blogKey.getBlogId());
        blogSelective.setCategoryId(categoryId);
        blogSelective.setGmtModified(new Date(System.currentTimeMillis()));
        blogMapper.updateByPrimaryKeySelective(blogSelective);

        changeBlogCategoryResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        changeBlogCategoryResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return changeBlogCategoryResponse;
    }

    /**
     * 删除博客(假删除，一定时间内可回收，超时真删除)
     *
     * @param deleteBlogRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public DeleteBlogResponse deleteBlog(DeleteBlogRequest deleteBlogRequest) {
        DeleteBlogResponse deleteBlogResponse = new DeleteBlogResponse();

        BlogIdEncrypt.BlogKey blogKey = BlogIdEncrypt.decrypt(deleteBlogRequest.getBlogId());
        Long userId = deleteBlogRequest.getUserId();

        Blog blog = Optional.ofNullable(blogKey)
                .map(BlogIdEncrypt.BlogKey::getBlogId)
                .map(blogMapper::selectByPrimaryKey)
                .filter(b -> Objects.equals(userId, b.getPkUserId()))
                .orElse(null);

        if (blog == null) {
            deleteBlogResponse.setCode(SysRetCodeConstants.BLOG_NOT_EXISTS.getCode());
            deleteBlogResponse.setMsg(SysRetCodeConstants.BLOG_NOT_EXISTS.getMsg());
            return deleteBlogResponse;
        }

        boolean isRecoverable = Objects.equals(BlogStatusEnum.RECOVERABLE.status(), blog.getStatus());
        if (! isRecoverable) {
            Blog blogSelective = new Blog();
            blogSelective.setId(blogKey.getBlogId());
            blogSelective.setStatus(BlogStatusEnum.RECOVERABLE.status());
            blogSelective.setGmtModified(new Date(System.currentTimeMillis()));
            blogMapper.updateByPrimaryKeySelective(blogSelective);
        }

        deleteBlogResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        deleteBlogResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return deleteBlogResponse;
    }

    /**
     * 回收博客
     *
     * @param recycleBlogRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public RecycleBlogResponse recycleBlog(RecycleBlogRequest recycleBlogRequest) {
        RecycleBlogResponse recycleBlogResponse = new RecycleBlogResponse();

        BlogIdEncrypt.BlogKey blogKey = BlogIdEncrypt.decrypt(recycleBlogRequest.getBlogId());
        Long userId = recycleBlogRequest.getUserId();

        Blog blog = Optional.ofNullable(blogKey)
                .map(BlogIdEncrypt.BlogKey::getBlogId)
                .map(blogMapper::selectByPrimaryKey)
                .filter(b -> Objects.equals(userId, b.getPkUserId()))
                .orElse(null);

        if (blog == null) {
            recycleBlogResponse.setCode(SysRetCodeConstants.BLOG_NOT_EXISTS.getCode());
            recycleBlogResponse.setMsg(SysRetCodeConstants.BLOG_NOT_EXISTS.getMsg());
            return recycleBlogResponse;
        }

        boolean isNormal = Objects.equals(BlogStatusEnum.NORMAL.status(), blog.getStatus());
        if (! isNormal) {
            Blog blogSelective = new Blog();
            blogSelective.setId(blogKey.getBlogId());
            blogSelective.setStatus(BlogStatusEnum.NORMAL.status());
            blogSelective.setGmtModified(new Date(System.currentTimeMillis()));
            blogMapper.updateByPrimaryKeySelective(blogSelective);
        }

        recycleBlogResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        recycleBlogResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return recycleBlogResponse;
    }

    /**
     * 判断分类是否存在
     * @param pkUserId
     * @param categoryId
     * @return
     */
    private boolean judgeCategoryIsExists(Long pkUserId, Long categoryId) {
        if (categoryId != null && categoryId.longValue() == 0L) {
            return true;
        }
        Long categoryOwnerId = blogCategoryDao.selectUserIdByPrimaryKey(categoryId);
        return categoryOwnerId != null && Objects.equals(categoryOwnerId, pkUserId);
    }

    /**
     * 处理 oss 引用
     * @param refs
     */
    private void processRef(Set<OssRefDTO> refs) {
        Date now = new Date(System.currentTimeMillis());
        refs.forEach(ref -> {
            if (ref == null) {
                return;
            }
            // 1.先判断是否已经在 oss 服务器创建引用
            Example example = new Example(BlogOssRef.class);
            example.createCriteria().andEqualTo("ossKey", ref.getObjectKey()).andEqualTo("ossBucket", ref.getBucketName());
            boolean hasRef = blogOssRefMapper.selectCountByExample(example) > 0;
            if (hasRef) {
                return;
            }

            final String DEL_KEY = RandomStringUtils.randomAlphabetic(OssConstants.OSS_DEL_KEY_LENGTH);
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
            blogOssRef.setOssBucket(ref.getBucketName());
            blogOssRef.setOssKey(ref.getObjectKey());
            blogOssRef.setOssRefId(refId);
            blogOssRef.setOssRefDelKey(DEL_KEY);
            blogOssRef.setGmtCreate(now);
            blogOssRef.setGmtModified(now);
            blogOssRefMapper.insertUseGeneratedKeys(blogOssRef);
        });
    }

    /**
     * 处理标签
     * @param blogId
     * @param labels
     */
    private void processLabels(Long blogId, Set<String> labels) {
        // 先删除所有标签
        blogLabelDao.deleteLabelByBlogId(blogId);
        // 再保存
        if (CollectionUtils.isEmpty(labels)) {
            return;
        }
        Date now = new Date(System.currentTimeMillis());
        labels.forEach(label -> {
                    if (StringUtils.isNotBlank(label)) {
                        BlogLabel blogLabel = new BlogLabel();
                        blogLabel.setBlogId(blogId);
                        blogLabel.setLabel(label);
                        blogLabel.setGmtCreate(now);
                        blogLabel.setGmtModified(now);
                        blogLabelMapper.insert(blogLabel);
                    }
                });
    }

}
