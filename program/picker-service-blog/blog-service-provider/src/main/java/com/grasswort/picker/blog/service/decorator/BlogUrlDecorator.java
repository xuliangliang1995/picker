package com.grasswort.picker.blog.service.decorator;

import com.grasswort.picker.blog.dao.entity.Blog;
import com.grasswort.picker.blog.util.BlogIdEncrypt;

/**
 * @author xuliangliang
 * @Classname BlogUrlDecorator
 * @Description 获取博客 URL 地址装饰器
 * @Date 2019/11/17 14:31
 * @blame Java Team
 */
public class BlogUrlDecorator extends Blog {

    private static final String URL_TEMPLATE = "https://grasswort.com/api/blog/%s.html";

    private Blog blog;

    public BlogUrlDecorator(Blog blog) {
        this.blog = blog;
    }

    /**
     * 获取博客地址
     * @return
     */
    public String url() {
        return String.format(URL_TEMPLATE, BlogIdEncrypt.encrypt(blog.getId()));
    }
    /**
     * 获取博客地址（版本信息）
     * @return
     */
    public String url(int version) {
        return String.format(URL_TEMPLATE, BlogIdEncrypt.encrypt(blog.getId(), version));
    }
}
