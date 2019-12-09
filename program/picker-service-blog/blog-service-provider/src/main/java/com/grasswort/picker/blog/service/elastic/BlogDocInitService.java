package com.grasswort.picker.blog.service.elastic;

import com.grasswort.picker.blog.dao.persistence.BlogMapper;
import com.grasswort.picker.blog.elastic.repository.BlogDocRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xuliangliang
 * @Classname BlogDocInitService
 * @Description
 * @Date 2019/12/9 11:25
 * @blame Java Team
 */
@Service
public class BlogDocInitService {

    @Resource
    BlogMapper blogMapper;

    @Resource
    BlogDocRepository blogDocRepository;

    @Resource
    BlogDocConverter blogDocConverter;

    /**
     * 初始化
     */
    public void init() {
        blogMapper.selectAll().forEach(blog -> blogDocRepository.save(blogDocConverter.blog2BlogDoc(blog)));
    }
}
