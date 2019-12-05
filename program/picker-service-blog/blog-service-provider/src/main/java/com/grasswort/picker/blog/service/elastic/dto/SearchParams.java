package com.grasswort.picker.blog.service.elastic.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname SearchParams
 * @Description 查询参数
 * @Date 2019/12/5 18:49
 * @blame Java Team
 */
@Data
@Builder
public class SearchParams {
    /**
     * 关键词
     */
    private String keyword;
    /**
     * 作者 id
     */
    private Long authorId;

}
