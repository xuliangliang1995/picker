package com.grasswort.picker.user.dto.user;

import lombok.Data;

/**
 * @author xuliangliang
 * @Classname InteractionData
 * @Description 交互数据
 * @Date 2019/12/3 22:57
 * @blame Java Team
 */
@Data
public class InteractionData {
    /**
     * 博客数量
     */
    private Long blogCount;
    /**
     * 专题数量
     */
    private Long topicCount;
    /**
     * 关注别人的数量
     */
    private Long subscribeCount;
    /**
     * 粉丝
     */
    private Long fansCount;
    /**
     * 获赞
     */
    private Long likedCount;

}
