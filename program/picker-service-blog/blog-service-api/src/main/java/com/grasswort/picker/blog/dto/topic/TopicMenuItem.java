package com.grasswort.picker.blog.dto.topic;

import lombok.Data;

import java.util.List;

/**
 * @author xuliangliang
 * @Classname TopicMenuItem
 * @Description 专题菜单
 * @Date 2019/12/11 15:26
 * @blame Java Team
 */
@Data
public class TopicMenuItem {

    private Long menuId;

    private Long parentMenuId;

    private String menuName;

    private String menuType;

    private String blogId;

    private List<TopicMenuItem> children;



}
