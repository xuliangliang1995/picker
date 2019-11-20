package com.grasswort.picker.blog.service;

import com.grasswort.picker.blog.IBlogPoolService;
import com.grasswort.picker.blog.constant.BlogStatusEnum;
import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.dao.entity.Blog;
import com.grasswort.picker.blog.dao.entity.BlogTrigger;
import com.grasswort.picker.blog.dao.persistence.BlogLabelMapper;
import com.grasswort.picker.blog.dao.persistence.BlogMapper;
import com.grasswort.picker.blog.dao.persistence.ext.BlogLabelDao;
import com.grasswort.picker.blog.dto.BlogPoolQueryRequest;
import com.grasswort.picker.blog.dto.BlogPoolQueryResponse;
import com.grasswort.picker.blog.dto.blog.BlogItemWithAuthor;
import com.grasswort.picker.blog.util.BlogIdEncrypt;
import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.user.IUserBaseInfoService;
import com.grasswort.picker.user.dto.UserBaseInfoRequest;
import com.grasswort.picker.user.dto.UserBaseInfoResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author xuliangliang
 * @Classname BlogPoolServiceImpl
 * @Description
 * @Date 2019/11/20 15:58
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 10000)
public class BlogPoolServiceImpl implements IBlogPoolService {

    @Autowired BlogMapper blogMapper;

    @Autowired BlogLabelMapper blogLabelMapper;

    @Autowired BlogLabelDao blogLabelDao;

    @Reference(version = "1.0", timeout = 10000) IUserBaseInfoService iUserBaseInfoService;

    /**
     * 获取博客列表
     *
     * @param queryRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public BlogPoolQueryResponse blogPool(BlogPoolQueryRequest queryRequest) {
        BlogPoolQueryResponse response = new BlogPoolQueryResponse();

        String keyword = queryRequest.getKeyword();
        Integer pageNo = queryRequest.getPageNo();
        Integer pageSize = queryRequest.getPageSize();

        RowBounds rowBounds = new RowBounds(pageSize * (pageNo - 1), pageSize);

        if (StringUtils.isNotBlank(keyword)) {
            // TODO 根据关键词搜索
        } else {
            Example example = new Example(Blog.class);
            example.createCriteria().andEqualTo("status", BlogStatusEnum.NORMAL.status());
            example.setOrderByClause("id desc");
            long total = blogMapper.selectCountByExample(example);
            List<BlogItemWithAuthor> blogs = blogMapper.selectByExampleAndRowBounds(example, rowBounds).parallelStream().map(blog -> {
                // 标签
                List<String> labels = blogLabelDao.listBlogLabels(blog.getId());
                // 博客推送状态
                Example ex = new Example(BlogTrigger.class);
                ex.createCriteria().andEqualTo("blogId", blog.getId());

                UserBaseInfoResponse baseInfoResponse = iUserBaseInfoService.userBaseInfo(UserBaseInfoRequest.Builder.anUserBaseInfoRequest().withUserId(blog.getPkUserId()).build());
                String author = Optional.ofNullable(baseInfoResponse).filter(UserBaseInfoResponse::isSuccess).map(UserBaseInfoResponse::getName).orElse("");
                String avatar = Optional.ofNullable(baseInfoResponse).filter(UserBaseInfoResponse::isSuccess).map(UserBaseInfoResponse::getAvatar).orElse("");
                return BlogItemWithAuthor.Builder.aBlogItemWithAuthor()
                        .withBlogId(BlogIdEncrypt.encrypt(blog.getId()))
                        .withTitle(blog.getTitle())
                        .withSummary(blog.getSummary())
                        .withCoverImg(blog.getCoverImg())
                        .withLabels(labels)
                        .withVersion(blog.getVersion())
                        .withGmtCreate(blog.getGmtCreate())
                        .withGmtModified(blog.getGmtModified())
                        .withAuthor(author)
                        .withAuthorAvatar(avatar)
                        .build();
            }).collect(Collectors.toList());
            response.setBlogs(blogs);
            response.setTotal(total);
        }

        response.setCode(SysRetCodeConstants.SUCCESS.getCode());
        response.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return response;
    }
}
