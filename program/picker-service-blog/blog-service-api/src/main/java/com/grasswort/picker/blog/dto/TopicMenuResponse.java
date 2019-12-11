package com.grasswort.picker.blog.dto;

import com.grasswort.picker.blog.dto.topic.TopicMenuItem;
import lombok.Data;

import java.util.List;

/**
 * @author xuliangliang
 * @Classname TopicMenuResponse
 * @Description
 * @Date 2019/12/11 15:34
 * @blame Java Team
 */
@Data
public class TopicMenuResponse extends AbstractBlogResponse {

    private List<TopicMenuItem> menu;

    private Boolean editable;

}
