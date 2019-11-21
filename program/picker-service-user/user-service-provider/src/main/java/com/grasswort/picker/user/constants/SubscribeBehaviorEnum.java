package com.grasswort.picker.user.constants;

/**
 * @author xuliangliang
 * @Classname SubscribeBehaviorEnum
 * @Description 关注行为
 * @Date 2019/11/21 14:47
 * @blame Java Team
 */
public enum SubscribeBehaviorEnum {
    SUBSCRIBE(1, "订阅"),
    UNSUBSCRIBE(2, "取消订阅");
    private int id;

    SubscribeBehaviorEnum(int id, String note) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
