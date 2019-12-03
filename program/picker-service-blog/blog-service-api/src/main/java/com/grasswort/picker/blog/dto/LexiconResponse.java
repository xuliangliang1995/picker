package com.grasswort.picker.blog.dto;

import lombok.Data;

import java.util.Set;

/**
 * @author xuliangliang
 * @Classname Lexicon
 * @Description 词库
 * @Date 2019/12/3 11:39
 * @blame Java Team
 */
@Data
public class LexiconResponse extends AbstractBlogResponse{

    private Set<String> lexicon;

    private long lastModified;

    private String eTag = "ik-word";
}
