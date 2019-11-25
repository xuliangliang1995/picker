package com.grasswort.picker.blog.service;

import com.grasswort.picker.blog.IBlogBrowseService;
import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.dao.entity.BlogBrowse;
import com.grasswort.picker.blog.dao.persistence.BlogBrowseMapper;
import com.grasswort.picker.blog.dto.BlogBrowseRequest;
import com.grasswort.picker.blog.dto.BlogBrowseResponse;
import com.grasswort.picker.blog.util.BlogIdEncrypt;
import com.grasswort.picker.commons.annotation.DB;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author xuliangliang
 * @Classname BlogBrowseServiceImpl
 * @Description 博客浏览
 * @Date 2019/11/25 16:57
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 10000)
public class BlogBrowseServiceImpl implements IBlogBrowseService {

    @Autowired BlogBrowseMapper blogBrowseMapper;
    /**
     * 博客浏览
     *
     * @param blogBrowseRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public BlogBrowseResponse browse(BlogBrowseRequest blogBrowseRequest) {
        BlogBrowseResponse blogBrowseResponse = new BlogBrowseResponse();

        BlogIdEncrypt.BlogKey blogKey = BlogIdEncrypt.decrypt(blogBrowseRequest.getBlogId());

        if (blogKey != null) {
            BlogBrowse browse = new BlogBrowse();
            browse.setBlogId(blogKey.getBlogId());
            Date now = new Date(System.currentTimeMillis());
            browse.setGmtCreate(now);
            browse.setGmtModified(now);
            blogBrowseMapper.insert(browse);
        }

        blogBrowseResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        blogBrowseResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return blogBrowseResponse;
    }
}
