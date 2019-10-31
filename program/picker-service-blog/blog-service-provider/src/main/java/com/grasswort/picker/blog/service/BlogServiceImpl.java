package com.grasswort.picker.blog.service;

import com.grasswort.picker.blog.IBlogService;
import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.dao.entity.Blog;
import com.grasswort.picker.blog.dao.persistence.BlogMapper;
import com.grasswort.picker.blog.dto.OwnBlogListRequest;
import com.grasswort.picker.blog.dto.OwnBlogListResponse;
import com.grasswort.picker.blog.dto.blog.BlogItem;
import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.commons.constants.cluster.ClusterFaultMechanism;
import org.apache.dubbo.config.annotation.Service;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xuliangliang
 * @Classname BlogServiceImpl
 * @Description 博客实现类
 * @Date 2019/10/30 22:23
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 5000, cluster = ClusterFaultMechanism.FAIL_OVER)
public class BlogServiceImpl implements IBlogService {

    @Autowired BlogMapper blogMapper;

    /**
     * 查看自己的博客列表
     *
     * @param ownBlogListRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public OwnBlogListResponse ownBlogList(OwnBlogListRequest ownBlogListRequest) {
        OwnBlogListResponse response = new OwnBlogListResponse();

        Long userId = ownBlogListRequest.getUserId();
        Long categoryId = ownBlogListRequest.getCategoryId();
        Integer pageNo = ownBlogListRequest.getPageNo();
        Integer pageSize = ownBlogListRequest.getPageSize();

        RowBounds rowBounds = new RowBounds(pageSize * (pageNo - 1), pageSize);
        Example example = new Example(Blog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("pkUserId", userId);
        if (categoryId != null && categoryId >= 0) {
            criteria.andEqualTo("categoryId", categoryId);
        }
        List<Blog> blogs = blogMapper.selectByExampleAndRowBounds(example, rowBounds);
        response.setCode(SysRetCodeConstants.SUCCESS.getCode());
        response.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        response.setBlogs(
                blogs.parallelStream().map(blog -> BlogItem.Builder.aBlogItem()
                        .withBlogId(blog.getId())
                        .withTitle(blog.getTitle())
                        .withVersion(blog.getVersion())
                        .withGmtCreate(blog.getGmtCreate())
                        .withGmtModified(blog.getGmtModified())
                        .build()
                ).collect(Collectors.toList())
        );
        return response;
    }
}
