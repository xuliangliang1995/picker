package com.grasswort.picker.blog.dto;

import com.grasswort.picker.blog.dto.blog.BlogItemRecycle;
import lombok.Data;

import java.util.List;

/**
 * @author xuliangliang
 * @Classname RecyclableBlogResponse
 * @Description
 * @Date 2019/12/5 13:51
 * @blame Java Team
 */
@Data
public class RecyclableBlogResponse extends AbstractBlogResponse {

    private List<BlogItemRecycle> blogs;

    private Long total;


}
