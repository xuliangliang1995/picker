package com.grasswort.picker.blog.dto;

import lombok.Data;

import java.util.LinkedList;

/**
 * @author xuliangliang
 * @Classname HotWordResponse
 * @Description 热词
 * @Date 2019/12/3 14:51
 * @blame Java Team
 */
@Data
public class HotWordResponse extends AbstractBlogResponse {
    /**
     * 热词
     */
    private LinkedList<String> hotwords;
}
